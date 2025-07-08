package com.pard.weact.habitPost.dto.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResultListDto {
    private String userName;
    private String imageUrl;
    private Long likeCount; // 좋아요 수
    private Long postId;
}