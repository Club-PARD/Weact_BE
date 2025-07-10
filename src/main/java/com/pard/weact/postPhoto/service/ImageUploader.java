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

        System.out.println("🪄 Uploading to S3:");
        System.out.println("- file name: " + imageFile.getHashedName());
        System.out.println("- contentType: " + imageFile.getContentType());
        System.out.println("- size: " + imageFile.getSize());

        try (final InputStream inputStream = imageFile.getInputStream()) {
            final PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, inputStream, metadata);
            s3Client.putObject(putObjectRequest); // ✅ 퍼블릭 ACL 제거됨
        } catch (final AmazonServiceException e) {
            e.printStackTrace();
            throw new RuntimeException("INVALID_IMAGE_PATH");
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException("INVALID_IMAGE");
        }

        return s3Client.getUrl(bucket, path).toString();
    }
    @Value("${cloud.aws.s3.profile-folder}")
    private String profileFolder;

    public String uploadProfileImage(final ImageFile imageFile) {
        final String path = profileFolder + imageFile.getHashedName();

        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());
        metadata.setContentLength(imageFile.getSize());
        metadata.setCacheControl(CACHE_CONTROL_VALUE);

        try (final InputStream inputStream = imageFile.getInputStream()) {
            final PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, inputStream, metadata);
            s3Client.putObject(putObjectRequest);
        } catch (final AmazonServiceException e) {
            throw new RuntimeException("INVALID_PROFILE_IMAGE_PATH");
        } catch (final IOException e) {
            throw new RuntimeException("INVALID_PROFILE_IMAGE");
        }

        return s3Client.getUrl(bucket, path).toString();
    }

    @Value("${cloud.aws.s3.basic}")
    private String basic;

    public String uploadBasicImage(final ImageFile imageFile) {
        final String path = basic + imageFile.getHashedName();

        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());
        metadata.setContentLength(imageFile.getSize());
        metadata.setCacheControl(CACHE_CONTROL_VALUE);

        try (final InputStream inputStream = imageFile.getInputStream()) {
            final PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, inputStream, metadata);
            s3Client.putObject(putObjectRequest);
        } catch (final AmazonServiceException e) {
            throw new RuntimeException("INVALID_PROFILE_IMAGE_PATH");
        } catch (final IOException e) {
            throw new RuntimeException("INVALID_PROFILE_IMAGE");
        }

        return s3Client.getUrl(bucket, path).toString();
    }
}
