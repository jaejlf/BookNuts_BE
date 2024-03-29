package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.debate.DebateRoom;
import team.nine.booknutsbackend.dto.request.DebateRoomRequest;
import team.nine.booknutsbackend.dto.response.DebateRoomResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.service.DebateService;
import team.nine.booknutsbackend.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static team.nine.booknutsbackend.enumerate.DebateStatus.getMsgByCode;
import static team.nine.booknutsbackend.enumerate.DebateType.getDebateType;

@RequiredArgsConstructor
@RestController
@RequestMapping("/debate")
public class DebateController {

    private final DebateService debateService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Object> createRoom(@RequestPart(value = "file", required = false) MultipartFile file,
                                             @RequestPart(value = "room") @Valid DebateRoomRequest debateRoomRequest,
                                             @AuthenticationPrincipal User user) {
        DebateRoom newRoom = debateService.createRoom(file, debateRoomRequest, user);
        DebateRoomResponse debateRoomResponse = debateService.enterRoom(newRoom, debateRoomRequest.isOpinion(), user);
        return ResponseEntity
                .status(CREATED)
                .body(ResultResponse.create("토론방 개설", debateRoomResponse));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Object> getRoomOne(@PathVariable Long roomId) {
        DebateRoom room = debateService.getRoom(roomId);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(roomId + "번 토론방 조회", DebateRoomResponse.of(room)));
    }

    @GetMapping("/canenter/{roomId}")
    public ResponseEntity<Object> canEnter(@PathVariable Long roomId) {
        DebateRoom room = debateService.getRoom(roomId);
        Map<String, Boolean> result = debateService.canEnter(room);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(roomId + "번 토론방 참여 가능 여부", result));
    }

    @PatchMapping("/enter/{roomId}")
    public ResponseEntity<Object> enterRoom(@PathVariable Long roomId,
                                            @RequestParam Boolean opinion,
                                            @AuthenticationPrincipal User user) {
        DebateRoom room = debateService.getRoom(roomId);
        DebateRoomResponse debateRoom = debateService.enterRoom(room, opinion, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(roomId + "번 토론방 참여 완료", debateRoom));
    }

    @PatchMapping("/exit/{roomId}")
    public ResponseEntity<Object> exitRoom(@PathVariable Long roomId,
                                           @AuthenticationPrincipal User user) {
        DebateRoom room = debateService.getRoom(roomId);
        debateService.exitRoom(room, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(roomId + "번 토론방 나가기"));
    }

    @PatchMapping("/update/{roomId}")
    public ResponseEntity<Object> changeStatus(@PathVariable Long roomId,
                                               @RequestParam int status,
                                               @AuthenticationPrincipal User user) {
        DebateRoom room = debateService.getRoom(roomId);
        DebateRoomResponse updatedRoom = debateService.changeStatus(room, status, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.update(roomId + "번 토론방 '" + getMsgByCode(status) + "'(으)로 상태 변경", updatedRoom));
    }

    @GetMapping("/list/{type}")
    public ResponseEntity<Object> getRoomListByType(@PathVariable int type) {
        Map<String, List<DebateRoomResponse>> roomList = debateService.getRoomListByType(type);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.update(getDebateType(type) + " 토론방 목록 조회", roomList));
    }

}