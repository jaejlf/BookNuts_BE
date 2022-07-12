package team.nine.booknutsbackend.dto.request;

import lombok.Builder;
import lombok.Getter;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.domain.archive.Archive;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class ArchiveRequest {

    @NotBlank String title;
    @NotBlank String content;

    public static Archive archiveRequest(ArchiveRequest archiveRequest, User user) {
        Archive archive = new Archive();
        archive.setTitle(archiveRequest.getTitle());
        archive.setContent(archiveRequest.getContent());
        archive.setOwner(user);

        return archive;
    }

}
