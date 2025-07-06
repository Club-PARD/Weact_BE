package com.pard.weact.habitPost.controller;


import com.pard.weact.habitPost.dto.req.CreateHabitPostDto;
import com.pard.weact.habitPost.dto.req.ReadAllInRoomRequest;
import com.pard.weact.habitPost.dto.req.ReadOneRequest;
import com.pard.weact.habitPost.dto.res.PostResultListDto;
import com.pard.weact.habitPost.dto.res.PostResultOneDto;
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
    public ResponseEntity<Long> createHabitPost(@ModelAttribute CreateHabitPostDto dto,
                                                @RequestParam("image") MultipartFile image) throws Exception {
        Long postId = habitPostService.createHabitPost(dto, image);
        return ResponseEntity.ok(postId);
    }

    @GetMapping("")
    public ResponseEntity<List<PostResultListDto>> readAllInRoom(@RequestParam Long roomId,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<PostResultListDto> result = habitPostService.readAllInRoom(roomId, date);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/one")
    public ResponseEntity<PostResultOneDto> readOne(@RequestParam Long postId,
                                                    @RequestParam Long memberId) {
        PostResultOneDto dto = habitPostService.readOneInRoom(memberId, postId);
        return ResponseEntity.ok(dto);
    }
}

