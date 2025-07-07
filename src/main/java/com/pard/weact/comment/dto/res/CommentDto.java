package com.pard.weact.comment.dto.res;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CommentDto {
    private String content;
    private String writerName;
    private LocalDateTime createdAt;
}
