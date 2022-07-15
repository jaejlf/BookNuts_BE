package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteUserService {

    private final AwsS3Service awsS3Service;
    private final FollowService followService;
    private final ReactionService reactionService;
    private final SeriesService seriesService;
    private final ArchiveService archiveService;
    private final UserRepository userRepository;

    //회원 탈퇴
    @Transactional
    public void deleteAccount(User user) {

        //프로필 이미지 버킷에서 삭제
        awsS3Service.deleteImg(user.getProfileImgUrl());

        //팔로우 삭제
        followService.deleteAllFollow(user);

        //시리즈, 아카이브 삭제
        seriesService.deleteAllSeries(user);
        archiveService.deleteAllArchive(user);

        //공감, 넛츠 삭제
        reactionService.deleteAllReaction(user);

        //유저 이용 불가 처리
        user.setUsername(null);
        user.setNickname(null);
        user.setRefreshToken("");
        user.setProfileImgUrl("");
        user.setEnabled(false);
        user.setRequestedDeleteAt(LocalDateTime.now());
        userRepository.save(user);
    }

    //탈퇴 요청 후 30일(한 달) 뒤, 이메일/로그인 아이디 정보 삭제
    @Transactional
    @Scheduled(cron = "0 0 22 * * *") //매일 22:00:00 (초, 분, 시, 일, 월, 주)
    public void deleteAccountUserInfoAfter30Days() {

        //오늘 날짜에서 한 달 전 00:00 ~ 23:59
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(30), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now().minusDays(30), LocalTime.of(23, 59, 59));

        log.info(LocalDate.now() + " 회원 탈퇴 스케쥴러 실행 완료");

        List<User> userList = userRepository.findAllByEnabledAndRequestedDeleteAtBetween(false, startDatetime, endDatetime);
        for (User user : userList) {
            user.setEmail(null);
            user.setLoginId(null);
            userRepository.save(user);
            log.info("userId = " + user.getUserId() + " 정보 삭제 완료");
        }
    }

}
