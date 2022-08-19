package team.nine.booknutsbackend.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginRequest {
    @NotBlank String id;
    @NotBlank String password;
}