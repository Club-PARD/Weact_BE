package com.pard.weact.habitPost.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pard.weact.habitPost.dto.req.CreateHabitPostDto;
import com.pard.weact.habitPost.dto.req.ReadAllInRoomRequest;
import com.pard.weact.habitPost.dto.req.ReadOneRequest;
import com.pard.weact.habitPost.dto.res.PostResultListDto;
import com.pard.weact.habitPost.dto.res.PostResultOneDto;
import com.pard.weact.habitPost.service.HabitPostService;
import com.pard.weact.habitPost.dto.req.UploadPhotoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/habit-post")
public class HabitPostController {

    private final HabitPostService habitPostService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/habit-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "습관 인증 업로드")
    public ResponseEntity<Long> createHabitPost(
            @Parameter(
                    description = "습관 인증 요청 JSON (아래 예시처럼 입력)",
                    example = "{\"userId\": 2, \"roomId\": 1, \"message\": \"오늘도 성공!\", \"isHaemyeong\": true}"
            )
            @RequestPart("request") String requestJson,

            @Parameter(description = "이미지 파일")
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        CreateHabitPostDto request = objectMapper.readValue(requestJson, CreateHabitPostDto.class);

        Long postId = habitPostService.createHabitPost(request, image);
        return ResponseEntity.ok(postId);
    }


    @GetMapping("")
    public ResponseEntity<List<PostResultListDto>> readAllInRoom(@RequestParam Long roomId,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<PostResultListDto> result = habitPostService.readAllInRoom(roomId, date);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/one")
    public ResponseEntity<PostResultOneDto> readOne(@RequestParam Long userId,
                                                    @RequestParam Long postId) {
        PostResultOneDto dto = habitPostService.readOneInRoom(userId, postId);
        return ResponseEntity.ok(dto);
    }
}

