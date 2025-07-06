package com.pard.weact.comment.dto.res;

import com.pard.weact.comment.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CommentDto {
    private Long commentId;
    private String content;
    private String writerName;
    private LocalDateTime createdAt;
}

