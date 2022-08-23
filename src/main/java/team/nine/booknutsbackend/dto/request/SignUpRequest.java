package team.nine.booknutsbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank String loginId;
    @NotBlank String password;
    @NotBlank String username;
    @NotBlank String nickname;
    @NotBlank String email;
    String profileImgUrl;
}