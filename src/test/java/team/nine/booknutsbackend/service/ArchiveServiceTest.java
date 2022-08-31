package team.nine.booknutsbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import team.nine.booknutsbackend.domain.archive.Archive;
import team.nine.booknutsbackend.dto.request.ArchiveRequest;
import team.nine.booknutsbackend.dto.response.ArchiveResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("Archive 서비스")
class ArchiveServiceTest extends CommonServiceTest {

    @Autowired
    private ArchiveService archiveService;

    @MockBean
    private AwsS3Service awsS3Service;

    @DisplayName("아카이브 생성")
    @Test
    void createArchive() {
        //given
        ArchiveRequest archiveRequest = new ArchiveRequest("타이틀", "내용");
        given(awsS3Service.uploadImg(any(), any())).willReturn("");

        //when
        ArchiveResponse result = archiveService.createArchive(null, archiveRequest, user);

        //then
        ArchiveResponse expected = ArchiveResponse.of(new Archive(
                archiveRequest.getTitle(),
                archiveRequest.getContent(),
                user,
                ""
        ));

        assertAll(
                () -> assertThat(result.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(result.getContent()).isEqualTo(expected.getContent()),
                () -> assertThat(result.getImgUrl()).isEqualTo(expected.getImgUrl()),
                () -> assertThat(result.getArchiveCnt()).isEqualTo(expected.getArchiveCnt())
        );

    }

}