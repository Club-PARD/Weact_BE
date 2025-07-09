package com.pard.weact.defaultImage.controller;

import com.pard.weact.defaultImage.dto.res.LongIdDto;
import com.pard.weact.defaultImage.dto.res.PathDto;
import com.pard.weact.defaultImage.service.DefaultImageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/default-image")
@RequiredArgsConstructor
public class DefaultImageController {

    private final DefaultImageService defaultImageService;

    @PostMapping
    @Operation(summary = "기본 이미지 업로드")
    public ResponseEntity<LongIdDto> upload(@RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(defaultImageService.uploadDefaultImage(image));
    }

    @GetMapping("/{id}")
    @Operation(summary = "기본 이미지 ID로 조회")
    public ResponseEntity<PathDto> getImage(@PathVariable Long id) {
        return ResponseEntity.ok(defaultImageService.getImageById(id));
    }
}
