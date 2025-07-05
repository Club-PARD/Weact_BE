package com.pard.weact.liked.controller;

import com.pard.weact.liked.dto.res.LikeToggleDto;
import com.pard.weact.liked.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/toggle/{userId}/{postId}")
    public ResponseEntity<LikeToggleDto> toggleLike(@RequestParam String userId,@PathVariable Long postId) {
        LikeToggleDto response = likeService.toggleLike(userId, postId);
        return ResponseEntity.ok(response);
    }
}

