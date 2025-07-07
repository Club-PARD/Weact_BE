package com.pard.weact.comment.controller;

import com.pard.weact.comment.dto.req.CreateCommentDto;
import com.pard.weact.comment.dto.res.CommentDto;
import com.pard.weact.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@RequestBody CreateCommentDto dto) {
        CommentDto newComment = commentService.addComment(dto);
        return ResponseEntity.ok(newComment);
    }
}
