package com.pard.weact.habitPost.controller;


import com.pard.weact.habitPost.dto.req.CreateHabitPostDto;
import com.pard.weact.habitPost.dto.res.PostResultDto;
import com.pard.weact.habitPost.service.HabitPostService;
import com.pard.weact.habitPost.dto.req.UploadPhotoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/habit-post")
public class HabitPostController {

    private final HabitPostService habitPostService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createHabitPost(@RequestPart("file") MultipartFile file , @RequestPart("request") CreateHabitPostDto request) {
        UploadPhotoDto photo = new UploadPhotoDto(file);
        habitPostService.createPost(photo,request);
        return ResponseEntity.ok("게시글이 성공적으로 등록되었습니다.");
    }

    @GetMapping("")
    public ResponseEntity<List<PostResultDto>> readAllInRoom(
            @RequestParam Long roomId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date //2025-01-01 이런 식으로 넣으면 됨.
    ) {
        //like 추가
        List<PostResultDto> result = habitPostService.readAllInRoom(roomId,date);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/one")
    public ResponseEntity<PostResultDto> readOne(
            @RequestParam String userId,
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        // like 추가
        PostResultDto dto = habitPostService.readOneInRoom(userId, roomId, date);
        return ResponseEntity.ok(dto);
    }




}

