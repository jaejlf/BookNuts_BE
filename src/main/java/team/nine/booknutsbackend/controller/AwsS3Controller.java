package team.nine.booknutsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.exception.s3.EmptyFileException;
import team.nine.booknutsbackend.service.AwsS3Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("/img")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    //이미지 업로드
    @PostMapping("/upload")
    public String post( @RequestPart("file") MultipartFile file) throws EmptyFileException {
        return awsS3Service.uploadFile(file);
    }

}
