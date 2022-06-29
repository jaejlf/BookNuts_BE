package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.archive.Archive;
import team.nine.booknutsbackend.dto.request.ArchiveRequest;
import team.nine.booknutsbackend.dto.response.ArchiveResponse;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.exception.board.NoAccessException;
import team.nine.booknutsbackend.service.ArchiveService;
import team.nine.booknutsbackend.service.UserService;

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

    //아카이브 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<ArchiveResponse>> getArchiveList(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return new ResponseEntity<>(archiveService.getArchiveList(user), HttpStatus.OK);
    }

    //아카이브 생성
    @PostMapping("/create")
    public ResponseEntity<ArchiveResponse> createArchive(@RequestBody ArchiveRequest archiveRequest, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Archive newArchive = archiveService.createArchive(ArchiveRequest.newArchive(archiveRequest, user));
        return new ResponseEntity<>(ArchiveResponse.archiveResponse(newArchive), HttpStatus.CREATED);
    }

    //특정 아카이브 조회
    @GetMapping("/{archiveId}")
    public ResponseEntity<List<BoardResponse>> getArchive(@PathVariable Long archiveId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return new ResponseEntity<>(archiveService.getArchive(archiveId, user), HttpStatus.OK);
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
    public ResponseEntity<Object> deleteArchive(@PathVariable Long archiveId, Principal principal) throws NoAccessException {
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
        archiveService.deleteArchivePost(archiveId, boardId);

        Map<String, String> map = new HashMap<>();
        map.put("result", "삭제 완료");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //아카이브 수정
    @PatchMapping("/{archiveId}")
    public ResponseEntity<ArchiveResponse> updateArchive(@PathVariable Long archiveId, @RequestBody ArchiveRequest archiveRequest, Principal principal) throws NoAccessException {
        Archive archive = archiveService.findByArchiveId(archiveId);
        User user = userService.findUserByEmail(principal.getName());

        if (archiveRequest.getTitle() != null) archive.setTitle(archiveRequest.getTitle());
        if (archiveRequest.getContent() != null) archive.setContent(archiveRequest.getContent());
        if (archiveRequest.getImgUrl() != null) archive.setImgUrl(archiveRequest.getImgUrl());

        Archive updateArchive = archiveService.updateArchive(archive, user);
        return new ResponseEntity<>(ArchiveResponse.archiveResponse(updateArchive), HttpStatus.OK);
    }

}