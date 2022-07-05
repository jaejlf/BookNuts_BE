package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.domain.User;
import team.nine.booknutsbackend.service.AwsS3Service;
import team.nine.booknutsbackend.service.UserService;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/img")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;
    private final UserService userService;

    //이미지 업로드
    @PostMapping("/upload")
    public String uploadImg(@RequestPart("file") MultipartFile file, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return awsS3Service.uploadImg(file, user);
    }

}
