package com.pard.weact.comment.dto.req;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentDto {
    private Long postId;
    private Long userId;
    private String content;
}
