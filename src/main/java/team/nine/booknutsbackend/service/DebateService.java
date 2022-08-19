package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.debate.DebateRoom;
import team.nine.booknutsbackend.domain.debate.DebateUser;
import team.nine.booknutsbackend.dto.request.DebateRoomRequest;
import team.nine.booknutsbackend.dto.response.DebateRoomResponse;
import team.nine.booknutsbackend.exception.debate.CannotEnterException;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.repository.DebateRoomRepository;
import team.nine.booknutsbackend.repository.DebateUserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static team.nine.booknutsbackend.exception.ErrorMessage.*;

@RequiredArgsConstructor
@Service
public class DebateService {

    private final DebateRoomRepository debateRoomRepository;
    private final DebateUserRepository debateUserRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public DebateRoomResponse createRoom(MultipartFile file, DebateRoomRequest debateRoomRequest, User user) {
        DebateRoom debateRoom = new DebateRoom(
                debateRoomRequest,
                user,
                awsS3Service.uploadImg(file, "debate-")
        );
        return DebateRoomResponse.of(debateRoomRepository.save(debateRoom));
    }

    @Transactional
    public DebateRoomResponse enterRoom(Long roomId, boolean opinion, User user) {
        DebateRoom debateRoom = getRoom(roomId);
        checkRoomEnterEnable(user, debateRoom, opinion);

        DebateUser debateUser = new DebateUser(user, debateRoom, opinion);
        debateUserRepository.save(debateUser);
        return DebateRoomResponse.of(updateUserCount(debateRoom));
    }

    @Transactional(readOnly = true)
    public DebateRoomResponse getRoomOne(Long roomId) {
        DebateRoom debateRoom = getRoom(roomId);
        return DebateRoomResponse.of(debateRoom);
    }

    @Transactional(readOnly = true)
    public Map<String, Boolean> canEnter(Long roomId) {
        DebateRoom room = getRoom(roomId);
        int sideUser = room.getMaxUser() / 2;
        int curYesUser = room.getCurYesUser();
        int curNoUser = room.getCurNoUser();

        Map<String, Boolean> map = new LinkedHashMap<>();
        map.put("canJoinYes", sideUser > curYesUser);
        map.put("canJoinNo", sideUser > curNoUser);
        return map;
    }

    @Transactional
    public void exitRoom(Long roomId, User user) {
        DebateRoom debateRoom = getRoom(roomId);
        DebateUser debateUser = getDebateUser(debateRoom, user);
        debateUserRepository.delete(debateUser);
        updateUserCount(debateRoom);
    }

    @Transactional
    public DebateRoomResponse changeStatus(Long roomId, int status, User user) {
        DebateRoom room = getRoom(roomId);
        checkChangeEnable(status, user, room);
        room.changeStatus(status);
        return DebateRoomResponse.of(debateRoomRepository.save(room));
    }

    @Transactional
    public Map<String, List<DebateRoomResponse>> getRoomListByType(int type) {
        Map<String, List<DebateRoomResponse>> map = new LinkedHashMap<>();
        map.put("맞춤 토론", getCustomDebateRoomList(type));
        map.put("현재 진행 중인 토론", getIngDebateRoomList(type));
        map.put("현재 대기 중인 토론", getReadyDebateRoomList(type));
        return map;
    }

    private DebateRoom getRoom(Long roomId) {
        return debateRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(ROOM_NOT_FOUND.getMsg()));
    }

    private void checkRoomEnterEnable(User user, DebateRoom room, boolean opinion) {
        if (debateUserRepository.findByDebateRoomAndUser(room, user).isPresent()) {
            throw new CannotEnterException(DEBATE_USER_ALREADY_EXIST.getMsg());
        }
        if (room.getStatus() != 0) {
            throw new CannotEnterException(LOCKED_ROOM.getMsg());
        }
        if ((opinion && (room.getMaxUser() / 2 <= room.getCurYesUser())) || (!opinion && (room.getMaxUser() / 2 <= room.getCurNoUser()))) {
            throw new CannotEnterException(USER_EXCEED.getMsg());
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

    private void checkChangeEnable(int status, User user, DebateRoom room) {
        if (!Objects.equals(room.getOwner().getUserId(), user.getUserId())) {
            throw new NoAuthException(DEBATE_NO_AUTH.getMsg());
        }
        if (status <= 0 || status > 2) {
            throw new IllegalArgumentException(STATUS_NUM_ERROR.getMsg());
        }
    }

    //맞춤 토론방 목록
    private List<DebateRoomResponse> getCustomDebateRoomList(int type) {
        List<DebateRoom> debateRoomList = getDebateRoomList(type, 0);
        List<DebateRoomResponse> debateRoomResponseList = new ArrayList<>();
        int count = 0;
        for (DebateRoom debateRoom : debateRoomList) {
            debateRoomResponseList.add(DebateRoomResponse.of(debateRoom));
            count++;
            if (count == 3) break; //임의로, '토론 대기 중' 상태인 3개의 토론을 반환
        }
        return debateRoomResponseList;
    }

    //현재 진행 중인 토론방 목록
    private List<DebateRoomResponse> getIngDebateRoomList(int type) {
        List<DebateRoom> debateRoomList = getDebateRoomList(type, 1);
        List<DebateRoomResponse> debateRoomResponseList = new ArrayList<>();
        for (DebateRoom debateRoom : debateRoomList) {
            debateRoomResponseList.add(DebateRoomResponse.of(debateRoom));
        }
        Collections.reverse(debateRoomResponseList); //최신순
        return debateRoomResponseList;
    }

    //현재 대기 중인 토론방 목록
    private List<DebateRoomResponse> getReadyDebateRoomList(int type) {
        List<DebateRoom> debateRoomList = getDebateRoomList(type, 0);
        List<DebateRoomResponse> debateRoomResponseList = new ArrayList<>();

        for (DebateRoom debateRoom : debateRoomList) {
            debateRoomResponseList.add(DebateRoomResponse.of(debateRoom));
        }
        Collections.reverse(debateRoomResponseList); //최신순
        return debateRoomResponseList;
    }

    private List<DebateRoom> getDebateRoomList(int type, int status) {
        if (type == 2) return debateRoomRepository.findByStatus(1);
        else return debateRoomRepository.findByTypeAndStatus(type, status);
    }

}
