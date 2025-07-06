package com.pard.weact.postPhoto.service;

import com.pard.weact.habitPost.dto.req.UploadPhotoDto;
import com.pard.weact.postPhoto.entity.PostPhoto;
import com.pard.weact.postPhoto.repo.PostPhotoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostPhotoService {

    private final PostPhotoRepo postPhotoRepo;
    private final S3Uploader s3Uploader;

    public PostPhoto uploadAndSave(MultipartFile file, String directory) throws IOException {
        String url = s3Uploader.upload(file, directory);

        PostPhoto postPhoto = PostPhoto.builder()
                .original_name(file.getOriginalFilename())
                .unique_name(UUID.randomUUID().toString())
                .path(url)
                .build();

        return postPhotoRepo.save(postPhoto);
    }

    public String getPhotoPathById(Long photoId) {
        return postPhotoRepo.findById(photoId)
                .map(PostPhoto::getPath)
                .orElseThrow(() -> new IllegalArgumentException("이미지 정보가 없습니다."));
    }
}
