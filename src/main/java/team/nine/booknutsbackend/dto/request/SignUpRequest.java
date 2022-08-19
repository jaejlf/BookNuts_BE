package team.nine.booknutsbackend.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SignUpRequest {
    @NotBlank String loginId;
    @NotBlank String password;
    @NotBlank String username;
    @NotBlank String nickname;
    @NotBlank String email;
    String profileImgUrl;
}