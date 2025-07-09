package com.pard.weact.defaultImage.service;

import com.pard.weact.defaultImage.dto.res.LongIdDto;
import com.pard.weact.defaultImage.dto.res.PathDto;
import com.pard.weact.defaultImage.entity.DefaultImage;
import com.pard.weact.defaultImage.repo.DefaultImageRepo;
import com.pard.weact.postPhoto.dto.ImageFile;
import com.pard.weact.postPhoto.service.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultImageService {

    private final DefaultImageRepo defaultImageRepo;
    private final ImageUploader imageUploader;

    public LongIdDto uploadDefaultImage(MultipartFile file) {
        ImageFile imageFile = convertToImageFile(file);
        String url = imageUploader.uploadProfileImage(imageFile);

        DefaultImage saved = defaultImageRepo.save(DefaultImage.builder()
                .originalName(file.getOriginalFilename())
                .uniqueName(imageFile.getHashedName())
                .path(url)
                .build());

        return new LongIdDto(saved.getId());
    }

    public PathDto getImageById(Long id) {
        String path = defaultImageRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 이미지가 없습니다."))
                .getPath();

        return new PathDto(path);
    }

    private ImageFile convertToImageFile(MultipartFile multipartFile) {
        try {
            return new ImageFile(
                    UUID.randomUUID().toString(),
                    multipartFile.getContentType(),
                    multipartFile.getSize(),
                    multipartFile.getInputStream()
            );
        } catch (IOException e) {
            throw new RuntimeException("이미지를 읽는 데 실패했습니다.", e);
        }
    }
}
