package com.pard.weact.postPhoto.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.pard.weact.postPhoto.dto.ImageFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageUploader {

    private static final String CACHE_CONTROL_VALUE = "max-age=3153600";

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.folder}")
    private String folder;

    public List<String> uploadImages(final List<ImageFile> imageFiles) {
        return imageFiles.stream()
                .map(this::uploadImage)
                .toList();
    }

    private String uploadImage(final ImageFile imageFile) {
        final String path = folder + imageFile.getHashedName();

        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());
        metadata.setContentLength(imageFile.getSize());
        metadata.setCacheControl(CACHE_CONTROL_VALUE);

        try (final InputStream inputStream = imageFile.getInputStream()) {
            final PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead); // 🔑 공개 권한 설정

            s3Client.putObject(putObjectRequest); // S3에 업로드
        } catch (final AmazonServiceException e) {
            throw new RuntimeException("INVALID_IMAGE_PATH");
        } catch (final IOException e) {
            throw new RuntimeException("INVALID_IMAGE");
        }

        // 업로드된 파일의 전체 URL 반환
        return s3Client.getUrl(bucket, path).toString();
    }

}
