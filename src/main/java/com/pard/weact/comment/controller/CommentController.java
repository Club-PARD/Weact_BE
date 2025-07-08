package com.pard.weact.comment.controller;

import com.pard.weact.User.entity.User;
import com.pard.weact.comment.dto.req.CreateCommentDto;
import com.pard.weact.comment.dto.res.CommentDto;
import com.pard.weact.comment.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "jwtAuth")
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@AuthenticationPrincipal User user,
                                                 @RequestBody CreateCommentDto dto) {
        CommentDto newComment = commentService.addComment(user.getId(), dto);
        return ResponseEntity.ok(newComment);
    }
}
