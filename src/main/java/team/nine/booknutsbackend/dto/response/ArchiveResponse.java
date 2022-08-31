package team.nine.booknutsbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.nine.booknutsbackend.domain.archive.Archive;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveResponse {

    Long archiveId;
    String title;
    String content;
    String imgUrl;
    int archiveCnt;
    LocalDateTime createdAt;

    public static ArchiveResponse of(Archive archive) {
        return ArchiveResponse.builder()
                .archiveId(archive.getArchiveId())
                .title(archive.getTitle())
                .content(archive.getContent())
                .imgUrl(archive.getImgUrl())
                .archiveCnt(archive.getArchiveBoardList().size())
                .createdAt(archive.getCreatedDate())
                .build();
    }

}