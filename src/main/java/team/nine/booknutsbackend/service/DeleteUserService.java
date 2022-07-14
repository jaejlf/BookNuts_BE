package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.repository.UserRepository;

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

        userRepository.save(user);
    }

}
