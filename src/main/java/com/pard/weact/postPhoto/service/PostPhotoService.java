package com.pard.weact.postPhoto.service;

import com.pard.weact.postPhoto.entity.PostPhoto;
import com.pard.weact.postPhoto.dto.ImageFile;
import com.pard.weact.postPhoto.repo.PostPhotoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostPhotoService {

    private final PostPhotoRepo postPhotoRepo;
    private final ImageUploader imageUploader;

    public PostPhoto uploadAndSave(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }
        ImageFile imageFile = convertToImageFile(file);

        String url = imageUploader.uploadImages(List.of(imageFile)).get(0);  // 리스트로 넘기고 첫 번째 결과 받기

        PostPhoto postPhoto = PostPhoto.builder()
                .original_name(file.getOriginalFilename())
                .unique_name(imageFile.getHashedName())
                .path(url)
                .build();

        System.out.println("Saving photo: " + url); // 또는 photo.getId()
        return postPhotoRepo.save(postPhoto);
    }


    public String getPhotoPathById(Long photoId) {
        return postPhotoRepo.findById(photoId)
                .map(PostPhoto::getPath)
                .orElseThrow(() -> new IllegalArgumentException("이미지 정보가 없습니다."));
    }
    private ImageFile convertToImageFile(MultipartFile multipartFile) {
        try {
            return new ImageFile(
                    UUID.randomUUID().toString(),  // hashedName
                    multipartFile.getContentType(),
                    multipartFile.getSize(),
                    multipartFile.getInputStream()
            );
        } catch (IOException e) {
            throw new RuntimeException("이미지를 읽는 데 실패했습니다.", e);
        }
    }
    public PostPhoto getDefaultHaemyeongPhoto() {
        // 1. 고정된 해명 이미지 엔티티를 가져온다
        PostPhoto defaultOriginal = postPhotoRepo.findById(3L)
                .orElseThrow(() -> new IllegalArgumentException("기본 해명 이미지가 존재하지 않습니다."));

        // 2. 기존 정보를 기반으로 새로운 이미지 엔티티 생성
        PostPhoto copy = PostPhoto.builder()
                .original_name(defaultOriginal.getOriginal_name())
                .unique_name(UUID.randomUUID().toString()) // 새로 만든 uniqueName
                .path(defaultOriginal.getPath()) // 동일한 경로 사용
                .build();

        // 3. 새 이미지로 저장
        return postPhotoRepo.save(copy);
    }

}
