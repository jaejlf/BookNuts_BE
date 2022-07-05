package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.Board;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.series.Series;
import team.nine.booknutsbackend.domain.series.SeriesBoard;
import team.nine.booknutsbackend.dto.request.SeriesRequest;
import team.nine.booknutsbackend.dto.response.BoardResponse;
import team.nine.booknutsbackend.dto.response.SeriesResponse;
import team.nine.booknutsbackend.exception.board.BoardNotFoundException;
import team.nine.booknutsbackend.exception.user.NoAuthException;
import team.nine.booknutsbackend.exception.series.SeriesDuplicateException;
import team.nine.booknutsbackend.exception.series.SeriesNotFoundException;
import team.nine.booknutsbackend.repository.BoardRepository;
import team.nine.booknutsbackend.repository.SeriesBoardRepository;
import team.nine.booknutsbackend.repository.SeriesRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesBoardRepository seriesBoardRepository;
    private final BoardRepository boardRepository;
    private final BoardService boardService;

    //시리즈 목록 조회
    @Transactional(readOnly = true)
    public List<SeriesResponse> getSeriesList(User user) {
        List<Series> stories = seriesRepository.findAllByOwner(user);
        List<SeriesResponse> seriesResponseList = new ArrayList<>();

        for (Series series : stories) {
            seriesResponseList.add(SeriesResponse.seriesResponse(series));
        }

        Collections.reverse(seriesResponseList); //최신순
        return seriesResponseList;
    }

    //시리즈 발행
    @Transactional
    public Series createSeries(SeriesRequest seriesRequest, User user) {
        List<Long> boardIdList = seriesRequest.getBoardIdlist();
        Series series = seriesRepository.save(SeriesRequest.seriesRequest(seriesRequest, user));

        for (Long boardId : boardIdList) {
            SeriesBoard seriesBoard = new SeriesBoard();
            seriesBoard.setSeries(series);
            seriesBoard.setBoard(boardService.findBoard(boardId));
            seriesBoardRepository.save(seriesBoard);
        }

        return series;
    }

    //특정 시리즈 조회
    @Transactional(readOnly = true)
    public List<BoardResponse> getSeries(Long seriesId, User user) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(SeriesNotFoundException::new);
        List<SeriesBoard> seriesBoards = seriesBoardRepository.findBySeries(series);
        List<BoardResponse> boardList = new ArrayList<>();

        for (SeriesBoard seriesBoard : seriesBoards) {
            boardList.add(BoardResponse.boardResponse(seriesBoard.getBoard(), user));
        }

        Collections.reverse(boardList); //최신순
        return boardList;
    }

    //시리즈 삭제
    @Transactional
    public void deleteSeries(Long seriesId, User user) {
        Series series = seriesRepository.findBySeriesIdAndOwner(seriesId, user)
                .orElseThrow(NoAuthException::new);
        List<SeriesBoard> seriesBoards = seriesBoardRepository.findBySeries(series);

        seriesBoardRepository.deleteAll(seriesBoards);
        seriesRepository.delete(series);
    }

    //시리즈에 게시글 추가
    @Transactional
    public void addPostToSeries(Long seriesId, Long boardId) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(SeriesNotFoundException::new);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        //시리즈 중복체크
        if (seriesBoardRepository.findByBoardAndSeries(board, series).isPresent())
            throw new SeriesDuplicateException();

        SeriesBoard seriesBoard = new SeriesBoard();
        seriesBoard.setSeries(series);
        seriesBoard.setBoard(board);
        series.setOwner(series.getOwner());
        seriesBoardRepository.save(seriesBoard);
    }

}
