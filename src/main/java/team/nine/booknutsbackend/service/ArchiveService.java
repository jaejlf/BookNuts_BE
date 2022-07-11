package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.archive.Archive;
import team.nine.booknutsbackend.domain.archive.ArchiveBoard;
import team.nine.booknutsbackend.dto.request.ArchiveRequest;
import team.nine.booknutsbackend.dto.response.ArchiveResponse;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.exception.archive.ArchiveDuplicateException;
import team.nine.booknutsbackend.exception.archive.ArchiveNotFoundException;
import team.nine.booknutsbackend.exception.board.BoardNotFoundException;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.repository.ArchiveBoardRepository;
import team.nine.booknutsbackend.repository.ArchiveRepository;
import team.nine.booknutsbackend.repository.BoardRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final ArchiveBoardRepository archiveBoardRepository;
    private final BoardRepository boardRepository;

    //아카이브 조회
    @Transactional(readOnly = true)
    public Archive getArchive(Long archiveId) {
        return archiveRepository.findById(archiveId)
                .orElseThrow(ArchiveNotFoundException::new);
    }

    //특정 유저의 아카이브 목록 조회
    @Transactional(readOnly = true)
    public List<ArchiveResponse> getArchiveList(User owner) {
        List<Archive> archives = archiveRepository.findAllByOwner(owner);
        List<ArchiveResponse> archiveResponseList = new ArrayList<>();

        for (Archive archive : archives) {
            archiveResponseList.add(ArchiveResponse.archiveResponse(archive));
        }

        Collections.reverse(archiveResponseList); //최신순
        return archiveResponseList;
    }

    //아카이브 생성
    @Transactional
    public Archive createArchive(Archive archive) {
        return archiveRepository.save(archive);
    }

    //특정 아카이브 내의 게시글 조회
    @Transactional(readOnly = true)
    public List<BoardResponse> getArchiveBoards(Long archiveId, User user) {
        Archive archive = archiveRepository.findById(archiveId)
                .orElseThrow(ArchiveNotFoundException::new);
        List<ArchiveBoard> archiveBoards = archiveBoardRepository.findByArchive(archive);
        List<BoardResponse> boardList = new ArrayList<>();

        for (ArchiveBoard archiveBoard : archiveBoards) {
            boardList.add(BoardResponse.boardResponse(archiveBoard.getBoard(), user));
        }

        Collections.reverse(boardList); //최신순
        return boardList;
    }

    //아카이브에 게시글 추가
    @Transactional
    public void addPostToArchive(Long archiveId, Long boardId, User user) {
        Archive archive = getArchive(archiveId);
        if (archive.getOwner() != user) throw new NoAuthException();

        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        if (archiveBoardRepository.findByBoardAndOwner(board, user).isPresent())
            throw new ArchiveDuplicateException();

        ArchiveBoard archiveBoard = new ArchiveBoard();
        archiveBoard.setArchive(archive);
        archiveBoard.setBoard(board);
        archiveBoard.setOwner(archive.getOwner());
        archiveBoardRepository.save(archiveBoard);
    }

    //아카이브 삭제
    @Transactional
    public void deleteArchive(Long archiveId, User user) {
        Archive archive = getArchive(archiveId);
        if (archive.getOwner() != user) throw new NoAuthException();

        List<ArchiveBoard> archiveBoards = archiveBoardRepository.findByArchive(archive);

        archiveBoardRepository.deleteAll(archiveBoards);
        archiveRepository.delete(archive);
    }

    //아카이브 내의 게시글 삭제
    @Transactional
    public void deleteArchivePost(Long archiveId, Long boardId, User user) {
        Archive archive = getArchive(archiveId);
        if (archive.getOwner() != user) throw new NoAuthException();

        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        ArchiveBoard archiveBoard = archiveBoardRepository.findByArchiveAndBoard(archive, board);
        archiveBoardRepository.delete(archiveBoard);
    }

    //아카이브 수정
    @Transactional
    public Archive updateArchive(Long archiveId, ArchiveRequest archiveRequest, User user) {
        Archive archive = getArchive(archiveId);
        if (archive.getOwner() != user) throw new NoAuthException();

        if (archiveRequest.getTitle() != null) archive.setTitle(archiveRequest.getTitle());
        if (archiveRequest.getContent() != null) archive.setContent(archiveRequest.getContent());
        if (archiveRequest.getImgUrl() != null) archive.setImgUrl(archiveRequest.getImgUrl());

        return archiveRepository.save(archive);
    }

}