package com.pard.weact.liked.controller;

import com.pard.weact.User.entity.User;
import com.pard.weact.liked.dto.res.LikeToggleDto;
import com.pard.weact.liked.service.LikedService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
@SecurityRequirement(name = "jwtAuth")
public class LikeController {


    private final LikedService likedService;

    @PostMapping
    public ResponseEntity<LikeToggleDto> toggleLike(@AuthenticationPrincipal User user,
                                                    @RequestParam Long postId) {
        return ResponseEntity.ok(likedService.toggleLike(postId, user.getId()));
    }
}

