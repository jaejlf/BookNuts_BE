package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.archive.Archive;
import team.nine.booknutsbackend.domain.archive.ArchiveBoard;
import team.nine.booknutsbackend.dto.request.ArchiveRequest;
import team.nine.booknutsbackend.dto.response.ArchiveResponse;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.repository.ArchiveBoardRepository;
import team.nine.booknutsbackend.repository.ArchiveRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static team.nine.booknutsbackend.exception.ErrorMessage.*;

@RequiredArgsConstructor
@Service
public class ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final ArchiveBoardRepository archiveBoardRepository;
    private final AwsS3Service awsS3Service;

    @Transactional(readOnly = true)
    @Cacheable(key = "#user.nickname", value = "getArchiveList")
    public List<Archive> getArchiveList(User user) {
        return archiveRepository.findAllByOwner(user);
    }

    @Transactional
    public ArchiveResponse createArchive(MultipartFile file, ArchiveRequest archiveRequest, User user) {
        Archive archive = new Archive(
                archiveRequest,
                user,
                awsS3Service.uploadImg(file, "archive-")
        );
        return ArchiveResponse.of(archiveRepository.save(archive));
    }

    @Transactional(readOnly = true)
    public List<ArchiveBoard> getBoardsInArchive(Archive archive) {
        return archiveBoardRepository.findByArchive(archive);
    }

    @Transactional
    public void addBoardToArchive(Archive archive, Board board, User user) {
        checkAuth(archive, user);
        checkAddBoardEnable(user, board);

        ArchiveBoard archiveBoard = new ArchiveBoard(archive, board, archive.getOwner());
        archiveBoardRepository.save(archiveBoard);
    }

    @Transactional
    public void deleteArchive(Archive archive, User user) {
        checkAuth(archive, user);
        awsS3Service.deleteImg(archive.getImgUrl());  //기존 이미지 버킷에서 삭제
        archiveBoardRepository.deleteAllByArchiveId(archive.getArchiveId());
        archiveRepository.delete(archive);
    }

    @Transactional
    public void deleteBoardInArchive(Archive archive, Board board, User user) {
        checkAuth(archive, user);

        ArchiveBoard archiveBoard = archiveBoardRepository.findByArchiveAndBoard(archive, board);
        archiveBoardRepository.delete(archiveBoard);
    }

    @Transactional
    public ArchiveResponse updateArchive(Archive archive, Map<String, String> modRequest, User user) {
        checkAuth(archive, user);

        if (modRequest.get("title") != null) archive.updateTitle(modRequest.get("title"));
        if (modRequest.get("content") != null) archive.updateContent(modRequest.get("content"));

        return ArchiveResponse.of(archiveRepository.save(archive));
    }

    @Transactional
    public void deleteAllArchive(User user) {
        List<Archive> archiveList = archiveRepository.findAllByOwner(user);
        for (Archive archive : archiveList) {
            awsS3Service.deleteImg(archive.getImgUrl());  //기존 이미지 버킷에서 삭제
            archiveBoardRepository.deleteAllByArchiveId(archive.getArchiveId());
            archiveRepository.delete(archive);
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#archiveId", value = "getArchive")
    public Archive getArchive(Long archiveId) {
        return archiveRepository.findById(archiveId)
                .orElseThrow(() -> new EntityNotFoundException(ARCHIVE_NOT_FOUND.getMsg()));
    }

    private void checkAuth(Archive archive, User user) {
        if (!archive.getOwner().getNickname().equals(user.getNickname())) throw new NoAuthException(MOD_DEL_NO_AUTH.getMsg());
    }

    private void checkAddBoardEnable(User user, Board board) {
        if (archiveBoardRepository.findByBoardAndOwner(board, user).isPresent()) {
            throw new EntityExistsException(BOARD_ALREADY_EXIST.getMsg());
        }
    }

    public List<ArchiveResponse> entityToDto(List<Archive> archiveList) {
        return archiveList.stream().map(ArchiveResponse::of).collect(Collectors.toList());
    }

    public List<BoardResponse> entityToDto(List<ArchiveBoard> archiveBoardList, User user) {
        return archiveBoardList.stream().map(x -> BoardResponse.of(x.getBoard(), user)).collect(Collectors.toList());
    }

}