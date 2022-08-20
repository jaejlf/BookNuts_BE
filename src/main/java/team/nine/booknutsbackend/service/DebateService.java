package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.debate.DebateRoom;
import team.nine.booknutsbackend.domain.debate.DebateUser;
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

    //토론장 개설
    @Transactional
    public DebateRoom createRoom(MultipartFile file, DebateRoom newRoom) {
        newRoom.setCoverImgUrl(awsS3Service.uploadImg(file, "debate-"));
        return debateRoomRepository.save(newRoom);
    }

    //참여 가능 여부
    @Transactional(readOnly = true)
    public Object canEnter(Long roomId) {
        DebateRoom room = getRoom(roomId);
        int sideUser = room.getMaxUser() / 2;
        int curYesUser = room.getCurYesUser();
        int curNoUser = room.getCurNoUser();

        Map<String, Boolean> map = new LinkedHashMap<>();
        map.put("canJoinYes", sideUser > curYesUser);
        map.put("canJoinNo", sideUser > curNoUser);
        return map;
    }

    //토론장 참여
    @Transactional
    public DebateRoom enterRoom(Long roomId, User user, boolean opinion) {
        DebateRoom room = getRoom(roomId);

        if (debateUserRepository.findByDebateRoomAndUser(room, user).isPresent())
            throw new CannotEnterException(DEBATE_USER_ALREADY_EXIST.getMsg());
        if (room.getStatus() != 0) throw new CannotEnterException(LOCKED_ROOM.getMsg());
        if ((opinion && (room.getMaxUser() / 2 <= room.getCurYesUser())) || (!opinion && (room.getMaxUser() / 2 <= room.getCurNoUser())))
            throw new CannotEnterException(USER_EXCEED.getMsg());

        DebateUser debateUser = new DebateUser();
        debateUser.setUser(user);
        debateUser.setDebateRoom(room);
        debateUser.setOpinion(opinion);
        debateUserRepository.save(debateUser);

        return updateUserCount(room);
    }

    //토론 나가기
    @Transactional
    public void exitRoom(DebateRoom room, User user) {
        DebateUser debateUser = debateUserRepository.findByDebateRoomAndUser(room, user)
                .orElseThrow(() -> new EntityNotFoundException(DEBATE_USER_NOT_FOUND.getMsg()));
        debateUserRepository.delete(debateUser);
        updateUserCount(room);
    }

    //토론장 상태 변경
    @Transactional
    public DebateRoom changeStatus(Long roomId, int status, User user) {
        DebateRoom room = getRoom(roomId);

        if (room.getOwner().getUserId() != user.getUserId()) throw new NoAuthException(DEBATE_NO_AUTH.getMsg());
        if (status <= 0 || status > 2) throw new IllegalArgumentException(STATUS_NUM_ERROR.getMsg());

        room.setStatus(status);
        return debateRoomRepository.save(room);
    }

    //참여 유저 수 업데이트
    @Transactional
    public DebateRoom updateUserCount(DebateRoom room) {
        room.setCurYesUser(debateUserRepository.countByDebateRoomAndOpinion(room, true));
        room.setCurNoUser(debateUserRepository.countByDebateRoomAndOpinion(room, false));
        return debateRoomRepository.save(room);
    }

    //특정 토론장 조회
    @Transactional(readOnly = true)
    public DebateRoom getRoom(Long roomId) {
        return debateRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(ROOM_NOT_FOUND.getMsg()));
    }

    //맞춤 토론 리스트
    @Transactional(readOnly = true)
    public List<DebateRoomResponse> customDebate(int type) {
        List<DebateRoom> rooms;
        if (type == 2) rooms = debateRoomRepository.findByStatus(0);
        else rooms = debateRoomRepository.findByTypeAndStatus(type, 0);
        List<DebateRoomResponse> roomDtoList = new ArrayList<>();

        //임의로, '토론 대기 중' 상태인 3개의 토론을 반환
        int cnt = 0;
        for (DebateRoom room : rooms) {
            roomDtoList.add(DebateRoomResponse.roomResponse(room));
            cnt++;
            if (cnt == 3) break;
        }

        return roomDtoList;
    }

    //현재 진행 중인 토론 리스트
    @Transactional(readOnly = true)
    public List<DebateRoomResponse> ingDebate(int type) {
        List<DebateRoom> rooms;
        if (type == 2) rooms = debateRoomRepository.findByStatus(1);
        else rooms = debateRoomRepository.findByTypeAndStatus(type, 1);
        List<DebateRoomResponse> roomDtoList = new ArrayList<>();

        for (DebateRoom room : rooms) {
            roomDtoList.add(DebateRoomResponse.roomResponse(room));
        }
        Collections.reverse(roomDtoList); //최신순
        return roomDtoList;
    }

    //현재 대기 중인 토론 리스트
    @Transactional(readOnly = true)
    public List<DebateRoomResponse> readyDebate(int type) {
        List<DebateRoom> rooms;
        if (type == 2) rooms = debateRoomRepository.findByStatus(0);
        else rooms = debateRoomRepository.findByTypeAndStatus(type, 0);
        List<DebateRoomResponse> roomDtoList = new ArrayList<>();

        for (DebateRoom room : rooms) {
            roomDtoList.add(DebateRoomResponse.roomResponse(room));
        }
        Collections.reverse(roomDtoList); //최신순
        return roomDtoList;
    }

}
