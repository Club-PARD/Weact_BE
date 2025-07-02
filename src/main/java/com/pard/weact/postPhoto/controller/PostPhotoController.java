package com.pard.weact.postPhoto.controller;


import com.pard.weact.postPhoto.dto.req.UploadPostPhoto;
import com.pard.weact.postPhoto.service.PostPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PostPhotoController {

    private final PostPhotoService postPhotoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadHabit(@ModelAttribute UploadPostPhoto request) {
        MultipartFile image = request.getImage();
        String content = request.getContent();

        multipartService.save(image, content);

        return ResponseEntity.ok("습관 인증 업로드 완료!");
    }
}

