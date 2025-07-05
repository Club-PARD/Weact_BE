package com.pard.weact.postPhoto.controller;


import com.pard.weact.postPhoto.service.PostPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PostPhotoController {

    private final PostPhotoService postPhotoService;
    // image 잘 들어갔나 확인 용도
    @GetMapping("/image/{postPhotoId}")
    public ResponseEntity<byte[]> getImageById(@PathVariable Long postPhotoId) {
        return postPhotoService.getImageResponseById(postPhotoId);
    }

}

