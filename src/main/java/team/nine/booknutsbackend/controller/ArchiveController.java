package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.archive.Archive;
import team.nine.booknutsbackend.dto.request.ArchiveRequest;
import team.nine.booknutsbackend.dto.response.ArchiveResponse;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.service.ArchiveService;
import team.nine.booknutsbackend.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/archive")
public class ArchiveController {

    private final ArchiveService archiveService;
    private final UserService userService;

    //특정 유저의 아카이브 목록 조회
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<ArchiveResponse>> getArchiveList(@PathVariable Long userId) {
        User owner = userService.findUserById(userId);
        return new ResponseEntity<>(archiveService.getArchiveList(owner), HttpStatus.OK);
    }

    //아카이브 생성
    @PostMapping("/create")
    public ResponseEntity<ArchiveResponse> createArchive(@RequestPart(value = "file", required = false) MultipartFile file,
                                                         @RequestPart(value = "archive") @Valid ArchiveRequest archiveRequest,
                                                         Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Archive newArchive = archiveService.createArchive(file, ArchiveRequest.archiveRequest(archiveRequest, user));
        return new ResponseEntity<>(ArchiveResponse.archiveResponse(newArchive), HttpStatus.CREATED);
    }

    //특정 아카이브 내의 게시글 조회
    @GetMapping("/{archiveId}")
    public ResponseEntity<List<BoardResponse>> getArchiveBoards(@PathVariable Long archiveId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return new ResponseEntity<>(archiveService.getArchiveBoards(archiveId, user), HttpStatus.OK);
    }

    //아카이브에 게시글 추가
    @PatchMapping("/add/{archiveId}/{boardId}")
    public ResponseEntity<Object> addPostToArchive(@PathVariable Long archiveId, @PathVariable Long boardId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        archiveService.addPostToArchive(archiveId, boardId, user);

        Map<String, String> map = new HashMap<>();
        map.put("result", "추가 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //아카이브 삭제
    @DeleteMapping("/{archiveId}")
    public ResponseEntity<Object> deleteArchive(@PathVariable Long archiveId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        archiveService.deleteArchive(archiveId, user);

        Map<String, String> map = new HashMap<>();
        map.put("result", "삭제 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //아카이브 내의 게시글 삭제
    @DeleteMapping("/{archiveId}/{boardId}")
    public ResponseEntity<Object> deleteArchivePost(@PathVariable Long archiveId, @PathVariable Long boardId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        archiveService.deleteArchivePost(archiveId, boardId, user);

        Map<String, String> map = new HashMap<>();
        map.put("result", "삭제 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //아카이브 수정
    @PatchMapping("/{archiveId}")
    public ResponseEntity<ArchiveResponse> updateArchive(@PathVariable Long archiveId, @RequestBody ArchiveRequest archiveRequest, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Archive updateArchive = archiveService.updateArchive(archiveId, archiveRequest, user);
        return new ResponseEntity<>(ArchiveResponse.archiveResponse(updateArchive), HttpStatus.OK);
    }

}