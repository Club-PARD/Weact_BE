package com.pard.weact.habitPost.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pard.weact.User.entity.User;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "jwtAuth")
@RequestMapping("/habit-post")
public class HabitPostController {

    private final HabitPostService habitPostService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "습관 인증 업로드")
    public ResponseEntity<Long> createHabitPost(
            @AuthenticationPrincipal User user,
            @Parameter(
                    description = "습관 인증 요청 JSON 예시처럼 입력: {\"roomId\": 1, \"message\": \"오늘도 성공!\", \"isHaemyeong\": false}"
            )
            @RequestPart("request") String requestJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        CreateHabitPostDto request = objectMapper.readValue(requestJson, CreateHabitPostDto.class);

        Long postId;

        if (image == null || image.isEmpty()) {
            postId = habitPostService.createHabitPostWithoutPhoto(user.getId(), request);
        } else {
            postId = habitPostService.createHabitPost(user.getId(), request, image);
        }

        return ResponseEntity.ok(postId);
    }


    @GetMapping("")
    public ResponseEntity<List<PostResultListDto>> readAllInRoom(@RequestParam Long roomId,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<PostResultListDto> result = habitPostService.readAllInRoom(roomId, date);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/one")
    public ResponseEntity<PostResultOneDto> readOne(@AuthenticationPrincipal User user,
                                                    @RequestParam Long postId) {
        PostResultOneDto dto = habitPostService.readOneInRoom(user.getId(), postId);
        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler(ResponseStatusException.class) // 중복이 됐을때 중복됐다고 보여주는 예외처리
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}

