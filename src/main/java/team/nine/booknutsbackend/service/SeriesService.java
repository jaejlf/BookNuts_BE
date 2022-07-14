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
import team.nine.booknutsbackend.exception.board.BoardNotFoundException;
import team.nine.booknutsbackend.exception.series.SeriesDuplicateException;
import team.nine.booknutsbackend.exception.series.SeriesNotFoundException;
import team.nine.booknutsbackend.exception.user.NoAuthException;
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
    private final AwsS3Service awsS3Service;

    //시리즈 조회
    @Transactional(readOnly = true)
    public Series getSeries(Long seriesId) {
        return seriesRepository.findById(seriesId)
                .orElseThrow(SeriesNotFoundException::new);
    }

    //특정 유저의 시리즈 목록 조회
    @Transactional(readOnly = true)
    public List<SeriesResponse> getSeriesList(User owner) {
        List<Series> seriesList = seriesRepository.findAllByOwner(owner);
        List<SeriesResponse> seriesResponseList = new ArrayList<>();

        for (Series series : seriesList) {
            seriesResponseList.add(SeriesResponse.seriesResponse(series));
        }

        Collections.reverse(seriesResponseList); //최신순
        return seriesResponseList;
    }

    //시리즈 발행
    @Transactional
    public Series createSeries(MultipartFile file, Series series, List<Long> boardIdList) {
        series.setImgUrl(awsS3Service.uploadImg(file, "series-"));
        seriesRepository.save(series);

        for (Long boardId : boardIdList) {
            if (boardService.getPost(boardId).getUser() != series.getOwner()) continue;
            SeriesBoard seriesBoard = new SeriesBoard();
            seriesBoard.setSeries(series);
            seriesBoard.setBoard(boardService.getPost(boardId));
            seriesBoardRepository.save(seriesBoard);
        }

        return series;
    }

    //특정 시리즈 내의 게시글 조회
    @Transactional(readOnly = true)
    public List<BoardResponse> getSeriesBoards(Long seriesId, User user) {
        Series series = getSeries(seriesId);
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
        Series series = getSeries(seriesId);
        if (series.getOwner() != user) throw new NoAuthException();

        List<SeriesBoard> seriesBoards = seriesBoardRepository.findBySeries(series);
        seriesBoardRepository.deleteAll(seriesBoards);
        seriesRepository.delete(series);

        awsS3Service.deleteImg(series.getImgUrl());  //기존 이미지 버킷에서 삭제
    }

    //시리즈에 게시글 추가
    @Transactional
    public void addPostToSeries(Long seriesId, Long boardId, User user) {
        Series series = getSeries(seriesId);
        if (series.getOwner() != user) throw new NoAuthException();

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

    //시리즈 수정
    @Transactional
    public Series updateSeries(Long seriesId, SeriesRequest seriesRequest, User user) {
        Series series = getSeries(seriesId);
        if (series.getOwner() != user) throw new NoAuthException();

        if (seriesRequest.getTitle() != null) series.setTitle(seriesRequest.getTitle());
        if (seriesRequest.getContent() != null) series.setContent(seriesRequest.getContent());

        return seriesRepository.save(series);
    }

    //회원 탈퇴 시, 모든 시리즈 삭제
    @Transactional
    public void deleteAllSeries(User user) {
        List<Series> seriesList = seriesRepository.findAllByOwner(user);
        for (Series series : seriesList) {
            List<SeriesBoard> seriesBoards = seriesBoardRepository.findBySeries(series);
            seriesBoardRepository.deleteAll(seriesBoards);
            awsS3Service.deleteImg(series.getImgUrl());  //기존 이미지 버킷에서 삭제
        }
        seriesRepository.deleteAll(seriesList);
    }

}
