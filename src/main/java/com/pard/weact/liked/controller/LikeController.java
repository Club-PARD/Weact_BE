package com.pard.weact.liked.controller;

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
    public ResponseEntity<Void> like(@RequestParam Long postId, @RequestParam Long memberId) {
        likedService.like(postId, memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlike(@RequestParam Long postId, @RequestParam Long memberId) {
        likedService.unlike(postId, memberId);
        return ResponseEntity.ok().build();
    }
}

