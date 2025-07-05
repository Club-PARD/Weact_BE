package com.pard.weact.habitPost.dto.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResultOneDto {
    private String userName;
    private String message;
    private String imageUrl;
    private Long likeCount; // 좋아요 수
    private Boolean liked;  // 현재 로그인 유저가 누른 상태인지 여부 (readOne에서만 세팅됨)
}
