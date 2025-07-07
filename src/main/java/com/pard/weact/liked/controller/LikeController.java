package com.pard.weact.liked.controller;

import com.pard.weact.liked.dto.res.LikeToggleDto;
import com.pard.weact.liked.service.LikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {


    private final LikedService likedService;

    @PostMapping
    public ResponseEntity<LikeToggleDto> toggleLike(@RequestParam Long postId, @RequestParam Long userId) {
        return ResponseEntity.ok(likedService.toggleLike(postId, userId));
    }
}

