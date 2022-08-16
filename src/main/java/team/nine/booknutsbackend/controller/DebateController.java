package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.debate.DebateRoom;
import team.nine.booknutsbackend.dto.request.DebateRoomRequest;
import team.nine.booknutsbackend.dto.response.DebateRoomResponse;
import team.nine.booknutsbackend.service.DebateService;
import team.nine.booknutsbackend.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/debate")
public class DebateController {

    private final DebateService debateService;
    private final UserService userService;

    //토론장 개설
    @PostMapping("/create")
    public ResponseEntity<DebateRoomResponse> createRoom(@RequestPart(value = "file", required = false) MultipartFile file,
                                                         @RequestPart(value = "room") @Valid DebateRoomRequest room, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        DebateRoom newRoom = debateService.createRoom(file, DebateRoomRequest.roomRequest(room, user));
        DebateRoom saveRoom = debateService.enterRoom(newRoom.getDebateRoomId(), user, room.isOpinion());
        return new ResponseEntity<>(DebateRoomResponse.roomResponse(saveRoom), HttpStatus.CREATED);
    }

    //특정 토론장 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<DebateRoomResponse> getRoom(@PathVariable Long roomId) {
        return new ResponseEntity<>(DebateRoomResponse.roomResponse(debateService.getRoom(roomId)), HttpStatus.OK);
    }

    //참여 가능 여부
    @GetMapping("/canenter/{roomId}")
    public ResponseEntity<Object> canEnter(@PathVariable Long roomId) {
        return new ResponseEntity<>(debateService.canEnter(roomId), HttpStatus.OK);
    }

    //토론 참여
    @PatchMapping("/enter/{roomId}")
    public ResponseEntity<DebateRoomResponse> enterRoom(@PathVariable Long roomId, @RequestParam Boolean opinion, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        DebateRoom room = debateService.enterRoom(roomId, user, opinion);
        return new ResponseEntity<>(DebateRoomResponse.roomResponse(room), HttpStatus.OK);
    }

    //토론 나가기
    @PatchMapping("/exit/{roomId}")
    public ResponseEntity<Object> exitRoom(@PathVariable Long roomId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        debateService.exitRoom(debateService.getRoom(roomId), user);

        Map<String, String> map = new HashMap<>();
        map.put("result", "나가기 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //토론장 상태 변경
    @PatchMapping("/update/{roomId}")
    public ResponseEntity<DebateRoomResponse> changeStatus(@PathVariable Long roomId, @RequestParam int status, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        DebateRoom updateRoom = debateService.changeStatus(roomId, status, user);
        return new ResponseEntity<>(DebateRoomResponse.roomResponse(updateRoom), HttpStatus.OK);
    }

    //토론장 목록 조회
    //텍스트 = 0, 음성 = 1, 전체 = 2
    @GetMapping("/list/{type}")
    public ResponseEntity<Object> roomList(@PathVariable int type) {
        Map<String, List<DebateRoomResponse>> map = new LinkedHashMap<>();
        map.put("맞춤 토론", debateService.customDebate(type));
        map.put("현재 진행 중인 토론", debateService.ingDebate(type));
        map.put("현재 대기 중인 토론", debateService.readyDebate(type));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
