package team.nine.booknutsbackend.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.nine.booknutsbackend.domain.archive.Archive;

@Getter
@Builder
public class ArchiveResponse {

    Long archiveId;
    String title;
    String content;
    String imgUrl;
    int archiveCnt;
    String createdAt;

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