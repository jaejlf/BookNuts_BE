package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.series.Series;
import team.nine.booknutsbackend.domain.series.SeriesBoard;
import team.nine.booknutsbackend.dto.request.SeriesRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.SeriesResponse;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.repository.SeriesBoardRepository;
import team.nine.booknutsbackend.repository.SeriesRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static team.nine.booknutsbackend.exception.ErrorMessage.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesBoardRepository seriesBoardRepository;
    private final BoardService boardService;
    private final AwsS3Service awsS3Service;

    @Transactional(readOnly = true)
    @Cacheable(key = "#user.nickname", value = "getSeriesList")
    public List<SeriesResponse> getSeriesList(User user) {
        List<Series> seriesList = seriesRepository.findAllByOwner(user);
        List<SeriesResponse> seriesResponseList = entityToDto(seriesList);
        Collections.reverse(seriesResponseList); //최신순
        return seriesResponseList;
    }

    @Transactional
    public SeriesResponse createSeries(MultipartFile file, SeriesRequest seriesRequest, User user) {
        Series series = new Series(
                seriesRequest,
                user,
                awsS3Service.uploadImg(file, "series-")
        );
        seriesRepository.save(series);

        for (Long boardId : seriesRequest.getBoardIdlist()) {
            Board board = boardService.getBoard(boardId);
            if (!board.getWriter().getNickname().equals(series.getOwner().getNickname())) {
                log.warn(boardId + "번 게시글은 본인이 작성한 게시글이 아니므로 추가하지 않는다");
                continue;
            }
            SeriesBoard seriesBoard = new SeriesBoard(series, board);
            seriesBoardRepository.save(seriesBoard);
        }
        return SeriesResponse.of(series);
    }

    @Transactional(readOnly = true)
    public List<SeriesBoard> getBoardsInSeries(Series series) {
        return seriesBoardRepository.findBySeries(series);
    }

    @Transactional
    public void deleteSeries(Series series, User user) {
        checkAuth(series, user);
        awsS3Service.deleteImg(series.getImgUrl());  //기존 이미지 버킷에서 삭제
        seriesBoardRepository.deleteAllBySeriesId(series.getSeriesId());
        seriesRepository.delete(series);
    }

    @Transactional
    public void addBoardToSeries(Series series, Board board, User user) {
        checkAuth(series, user);
        checkAddBoardEnable(series, board);

        SeriesBoard seriesBoard = new SeriesBoard(series, board);
        seriesBoardRepository.save(seriesBoard);
    }

    @Transactional
    public Series updateSeries(Series series, Map<String, String> modRequest, User user) {
        checkAuth(series, user);

        if (modRequest.get("title") != null) series.updateTitle(modRequest.get("title"));
        if (modRequest.get("content") != null) series.updateContent(modRequest.get("content"));

        return seriesRepository.save(series);
    }

    @Transactional
    public void deleteAllSeries(User user) {
        List<Series> seriesList = seriesRepository.findAllByOwner(user);
        for (Series series : seriesList) {
            awsS3Service.deleteImg(series.getImgUrl());  //기존 이미지 버킷에서 삭제
            seriesBoardRepository.deleteAllBySeriesId(series.getSeriesId());
            seriesRepository.delete(series);
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#seriesId", value = "getSeries")
    public Series getSeries(Long seriesId) {
        return seriesRepository.findById(seriesId)
                .orElseThrow(() -> new EntityNotFoundException(SERIES_NOT_FOUND.getMsg()));
    }

    private void checkAuth(Series series, User user) {
        if (!series.getOwner().getNickname().equals(user.getNickname())) throw new NoAuthException(MOD_DEL_NO_AUTH.getMsg());
    }

    private void checkAddBoardEnable(Series series, Board board) {
        if (seriesBoardRepository.findByBoardAndSeries(board, series).isPresent())
            throw new EntityExistsException(BOARD_ALREADY_EXIST.getMsg());
    }

    public List<SeriesResponse> entityToDto(List<Series> seriesList) {
        return seriesList.stream().map(SeriesResponse::of).collect(Collectors.toList());
    }

    public List<BoardResponse> entityToDto(List<SeriesBoard> seriesBoardList, User user) {
        return seriesBoardList.stream().map(x -> BoardResponse.of(x.getBoard(), user)).collect(Collectors.toList());
    }

}
