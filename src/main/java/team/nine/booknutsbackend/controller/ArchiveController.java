package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.dto.request.ArchiveRequest;
import team.nine.booknutsbackend.dto.response.ArchiveResponse;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.ResultResponse;
import team.nine.booknutsbackend.service.ArchiveService;
import team.nine.booknutsbackend.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/archive")
public class ArchiveController {

    private final ArchiveService archiveService;
    private final UserService userService;

    @GetMapping("/list/{userId}")
    public ResponseEntity<Object> getArchiveList(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<ArchiveResponse> archiveList = archiveService.getArchiveList(user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(userId + "번 유저의 아카이브 목록 조회", archiveList));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createArchive(@RequestPart(value = "file", required = false) MultipartFile file,
                                                @RequestPart(value = "archive") @Valid ArchiveRequest archiveRequest,
                                                @AuthenticationPrincipal User user) {
        ArchiveResponse newArchive = archiveService.createArchive(file, archiveRequest, user);
        return ResponseEntity
                .status(CREATED)
                .body(ResultResponse.create("아카이브 생성", newArchive));
    }

    @GetMapping("/{archiveId}")
    public ResponseEntity<Object> getBoardsInArchive(@PathVariable Long archiveId,
                                                     @AuthenticationPrincipal User user) {
        List<BoardResponse> boardsInArchiveList = archiveService.getBoardsInArchive(archiveId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(archiveId + "번 아카이브 내의 게시글 조회", boardsInArchiveList));
    }

    @PatchMapping("/add/{archiveId}/{boardId}")
    public ResponseEntity<Object> addBoardToArchive(@PathVariable Long archiveId,
                                                    @PathVariable Long boardId,
                                                    @AuthenticationPrincipal User user) {
        archiveService.addBoardToArchive(archiveId, boardId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.update(archiveId + "번 아카이브에 " + boardId + "번 게시글 추가"));
    }

    @DeleteMapping("/{archiveId}")
    public ResponseEntity<Object> deleteArchive(@PathVariable Long archiveId,
                                                @AuthenticationPrincipal User user) {
        archiveService.deleteArchive(archiveId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.update(archiveId + "번 아카이브 삭제"));
    }

    @DeleteMapping("/{archiveId}/{boardId}")
    public ResponseEntity<Object> deleteBoardInArchive(@PathVariable Long archiveId,
                                                       @PathVariable Long boardId,
                                                       @AuthenticationPrincipal User user) {
        archiveService.deleteBoardInArchive(archiveId, boardId, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.ok(archiveId + "번 아카이브 내의 " + boardId + "번 게시글 삭제"));
    }

    @PatchMapping("/{archiveId}")
    public ResponseEntity<Object> updateArchive(@PathVariable Long archiveId,
                                                @RequestBody Map<String, String> modRequest,
                                                @AuthenticationPrincipal User user) {
        ArchiveResponse updatedArchive = archiveService.updateArchive(archiveId, modRequest, user);
        return ResponseEntity
                .status(OK)
                .body(ResultResponse.update(archiveId + "번 아카이브 수정", updatedArchive));
    }

}