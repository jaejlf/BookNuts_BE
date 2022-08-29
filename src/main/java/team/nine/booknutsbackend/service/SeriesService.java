package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
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
@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesBoardRepository seriesBoardRepository;
    private final BoardService boardService;
    private final AwsS3Service awsS3Service;

    @Transactional(readOnly = true)
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
            if (board.getWriter() != series.getOwner()) continue;
            SeriesBoard seriesBoard = new SeriesBoard(series, board);
            seriesBoardRepository.save(seriesBoard);
        }
        return SeriesResponse.of(series);
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getBoardsInSeries(Long seriesId, User user) {
        Series series = getSeries(seriesId);
        List<SeriesBoard> seriesBoardList = seriesBoardRepository.findBySeries(series);
        List<BoardResponse> boardList = entityToDto(seriesBoardList, user);
        Collections.reverse(boardList); //최신순
        return boardList;
    }

    @Transactional
    public void deleteSeries(Long seriesId, User user) {
        Series series = getSeries(seriesId);
        checkAuth(series, user);

        List<SeriesBoard> seriesBoardList = seriesBoardRepository.findBySeries(series);
        seriesBoardRepository.deleteAll(seriesBoardList);
        seriesRepository.delete(series);

        awsS3Service.deleteImg(series.getImgUrl());  //기존 이미지 버킷에서 삭제
    }

    @Transactional
    public void addBoardToSeries(Long seriesId, Long boardId, User user) {
        Series series = getSeries(seriesId);
        checkAuth(series, user);
        Board board = boardService.getBoard(boardId);
        checkAddBoardEnable(series, board);

        SeriesBoard seriesBoard = new SeriesBoard(series, board);
        seriesBoardRepository.save(seriesBoard);
    }

    @Transactional
    public Series updateSeries(Long seriesId, Map<String, String> modRequest, User user) {
        Series series = getSeries(seriesId);
        checkAuth(series, user);

        if (modRequest.get("title") != null) series.updateTitle(modRequest.get("title"));
        if (modRequest.get("content") != null) series.updateContent(modRequest.get("content"));

        return seriesRepository.save(series);
    }

    @Transactional
    public void deleteAllSeries(User user) {
        List<Series> seriesList = seriesRepository.findAllByOwner(user);
        for (Series series : seriesList) {
            List<SeriesBoard> seriesBoardList = seriesBoardRepository.findBySeries(series);
            seriesBoardRepository.deleteAll(seriesBoardList);
            awsS3Service.deleteImg(series.getImgUrl());  //기존 이미지 버킷에서 삭제
        }
        seriesRepository.deleteAll(seriesList);
    }

    private Series getSeries(Long seriesId) {
        return seriesRepository.findById(seriesId)
                .orElseThrow(() -> new EntityNotFoundException(SERIES_NOT_FOUND.getMsg()));
    }

    private void checkAuth(Series series, User user) {
        if (series.getOwner() != user) throw new NoAuthException(MOD_DEL_NO_AUTH.getMsg());
    }

    private void checkAddBoardEnable(Series series, Board board) {
        if (seriesBoardRepository.findByBoardAndSeries(board, series).isPresent())
            throw new EntityExistsException(BOARD_ALREADY_EXIST.getMsg());
    }

    private List<SeriesResponse> entityToDto(List<Series> seriesList) {
        return seriesList.stream().map(SeriesResponse::of).collect(Collectors.toList());
    }

    private List<BoardResponse> entityToDto(List<SeriesBoard> seriesBoardList, User user) {
        return seriesBoardList.stream().map(x -> BoardResponse.of(x.getBoard(), user)).collect(Collectors.toList());
    }

}
