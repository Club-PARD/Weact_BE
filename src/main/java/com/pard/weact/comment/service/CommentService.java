package com.pard.weact.comment.service;

import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.comment.dto.req.CreateCommentDto;
import com.pard.weact.comment.dto.res.CommentDto;
import com.pard.weact.comment.entity.Comment;
import com.pard.weact.comment.repo.CommentRepo;
import com.pard.weact.habitPost.entity.HabitPost;
import com.pard.weact.habitPost.repo.HabitPostRepo;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.memberInformation.repository.MemberInformationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;
    private final HabitPostRepo habitPostRepo;
    private final UserRepo userRepo;

    public CommentDto addComment(CreateCommentDto dto) {
        HabitPost post = habitPostRepo.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        User writer = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .habitPost(post)
                .user(writer)
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        commentRepo.save(comment);

        return new CommentDto(
                comment.getContent(),
                writer.getUserName(),
                comment.getCreatedAt()
        );
    }
}
