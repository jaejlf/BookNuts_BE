package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.debate.DebateRoom;
import team.nine.booknutsbackend.domain.debate.DebateUser;
import team.nine.booknutsbackend.dto.request.DebateRoomRequest;
import team.nine.booknutsbackend.dto.response.DebateRoomResponse;
import team.nine.booknutsbackend.enumerate.DebateStatus;
import team.nine.booknutsbackend.enumerate.DebateType;
import team.nine.booknutsbackend.exception.debate.CannotEnterException;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.repository.DebateRoomRepository;
import team.nine.booknutsbackend.repository.DebateUserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static team.nine.booknutsbackend.enumerate.DebateStatus.*;
import static team.nine.booknutsbackend.enumerate.DebateType.ALL;
import static team.nine.booknutsbackend.enumerate.DebateType.getDebateType;
import static team.nine.booknutsbackend.exception.ErrorMessage.*;

@RequiredArgsConstructor
@Service
public class DebateService {

    private final DebateRoomRepository debateRoomRepository;
    private final DebateUserRepository debateUserRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public DebateRoom createRoom(MultipartFile file, DebateRoomRequest debateRoomRequest, User user) {
        DebateRoom debateRoom = new DebateRoom(
                debateRoomRequest,
                user,
                awsS3Service.uploadImg(file, "debate-")
        );
        return debateRoomRepository.save(debateRoom);
    }

    @Transactional
    public DebateRoomResponse enterRoom(DebateRoom room, boolean opinion, User user) {
        checkRoomEnterEnable(user, room, opinion);

        DebateUser debateUser = new DebateUser(user, room, opinion);
        debateUserRepository.save(debateUser);
        return DebateRoomResponse.of(updateUserCount(room));
    }

    @Transactional(readOnly = true)
    public Map<String, Boolean> canEnter(DebateRoom room) {
        int sideUser = room.getMaxUser() / 2;
        int curYesUser = room.getCurYesUser();
        int curNoUser = room.getCurNoUser();

        Map<String, Boolean> map = new LinkedHashMap<>();
        map.put("canJoinYes", sideUser > curYesUser);
        map.put("canJoinNo", sideUser > curNoUser);
        return map;
    }

    @Transactional
    public void exitRoom(DebateRoom room, User user) {
        DebateUser debateUser = getDebateUser(room, user);
        debateUserRepository.delete(debateUser);
        updateUserCount(room);
    }

    @Transactional
    public DebateRoomResponse changeStatus(DebateRoom room, int status, User user) {
        checkAuth(room, user);
        checkChangeEnable(status, room);
        room.changeStatus(getStatusByCode(status));
        return DebateRoomResponse.of(debateRoomRepository.save(room));
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#type", value = "getRoomListByType")
    public Map<String, List<DebateRoomResponse>> getRoomListByType(int type) {
        DebateType debateType = getDebateType(type);
        Map<String, List<DebateRoomResponse>> map = new LinkedHashMap<>();
        map.put("맞춤 토론", getCustomDebateRoomList(debateType));
        map.put("현재 진행 중인 토론", getDebateRoomResponses(debateType, ING));
        map.put("현재 대기 중인 토론", getDebateRoomResponses(debateType, READY));
        return map;
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#roomId", value = "getRoom")
    public DebateRoom getRoom(Long roomId) {
        return debateRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(ROOM_NOT_FOUND.getMsg()));
    }

    private void checkRoomEnterEnable(User user, DebateRoom room, boolean opinion) {
        if (debateUserRepository.findByDebateRoomAndUser(room, user).isPresent()) {
            throw new CannotEnterException(DEBATE_USER_ALREADY_EXIST.getMsg());
        }
        if (room.getStatus() != READY) {
            throw new CannotEnterException(LOCKED_ROOM.getMsg());
        }
        if ((opinion && (room.getMaxUser() / 2 <= room.getCurYesUser())) || (!opinion && (room.getMaxUser() / 2 <= room.getCurNoUser()))) {
            throw new CannotEnterException(DEBATE_USER_EXCEED.getMsg());
        }
    }

    private DebateRoom updateUserCount(DebateRoom debateRoom) {
        debateRoom.updateCurUser(
                debateUserRepository.countByDebateRoomAndOpinion(debateRoom, true),
                debateUserRepository.countByDebateRoomAndOpinion(debateRoom, false)
        );
        return debateRoomRepository.save(debateRoom);
    }

    private DebateUser getDebateUser(DebateRoom room, User user) {
        return debateUserRepository.findByDebateRoomAndUser(room, user)
                .orElseThrow(() -> new EntityNotFoundException(DEBATE_USER_NOT_FOUND.getMsg()));
    }

    private void checkAuth(DebateRoom room, User user) {
        if (!Objects.equals(room.getOwner().getNickname(), user.getNickname())) {
            throw new NoAuthException(MOD_DEL_NO_AUTH.getMsg());
        }
    }

    private void checkChangeEnable(int status, DebateRoom room) {
        int curStatus = room.getStatus().getStatusCode();
        if ((status <= curStatus) || (status <= 0 || status > 2)) {
            throw new IllegalArgumentException(STATUS_NUM_ERROR.getMsg());
        }
    }

    private List<DebateRoomResponse> getCustomDebateRoomList(DebateType type) {
        List<DebateRoom> debateRoomList = getDebateRoomList(type, READY);
        List<DebateRoomResponse> debateRoomResponseList = entityToDto(debateRoomList);
        return new ArrayList<>(debateRoomResponseList.subList(0, 3)); //임의로, '토론 대기 중' 상태인 3개의 토론을 반환
    }

    private List<DebateRoomResponse> getDebateRoomResponses(DebateType type, DebateStatus debateStatus) {
        List<DebateRoom> debateRoomList = getDebateRoomList(type, debateStatus);
        List<DebateRoomResponse> debateRoomResponseList = entityToDto(debateRoomList);
        Collections.reverse(debateRoomResponseList); //최신순
        return debateRoomResponseList;
    }

    private List<DebateRoom> getDebateRoomList(DebateType type, DebateStatus status) {
        if (type == ALL) return debateRoomRepository.findByStatus(status);
        else return debateRoomRepository.findByTypeAndStatus(type, status);
    }

    public List<DebateRoomResponse> entityToDto(List<DebateRoom> debateRoomList) {
        return debateRoomList.stream().map(DebateRoomResponse::of).collect(Collectors.toList());
    }

}