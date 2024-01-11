package com.umc.StudyFlexBE.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;



    public String upload(MultipartFile multipartFile, Long memberId){
        String fileName = memberId+"/"+ UUID.randomUUID() + ".jpg";
        if (multipartFile.isEmpty()) {
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, byteArrayInputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new BaseException(BaseResponseStatus.UPLOAD_FAILED);
        }

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }


    public void deleteImage(String imgUrl) {
        try {
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, imgUrl);
            if (isObjectExist) {
                amazonS3Client.deleteObject(bucket,imgUrl);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FILE_DELETE_FAILED);
        }
    }

}
