package team.nine.booknutsbackend.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.nine.booknutsbackend.exception.s3.UploadFailedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    //이미지 업로드
    public String uploadImg(MultipartFile file, String keymsg) {
        if (file.isEmpty()) return "";

        String fileName = keymsg + UUID.randomUUID();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new UploadFailedException();
        }

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    //이미지 삭제
    public void deleteImg(String originImgUrl) {
        if (originImgUrl.equals("")) return;

        try {
            amazonS3.deleteObject(bucketName, originImgUrl.split("/")[3]);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }
    }

}
