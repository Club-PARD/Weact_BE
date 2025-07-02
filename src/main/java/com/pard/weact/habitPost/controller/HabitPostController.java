package com.pard.weact.habitPost.controller;


import com.pard.weact.habitPost.dto.req.CreateHabitPostDto;
import com.pard.weact.habitPost.service.HabitPostService;
import com.pard.weact.habitPost.dto.req.UploadPhotoDto;
import com.pard.weact.postPhoto.service.PostPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/habit-post")
public class HabitPostController {

    private final HabitPostService habitPostService;
    private final PostPhotoService postPhotoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createHabitPost(@RequestParam("file") MultipartFile file , @RequestBody CreateHabitPostDto request) {
        UploadPhotoDto photo = new UploadPhotoDto(file);
        habitPostService.createPost(photo,request);
        return ResponseEntity.ok("게시글이 성공적으로 등록되었습니다.");
    }

}

