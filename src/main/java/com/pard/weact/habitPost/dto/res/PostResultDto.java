package com.pard.weact.habitPost.dto.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResultDto {
    private String userName;     // 글쓴이
    private String message;      // 인증 메시지
    private String imageUrl;     // 이미지 경로
    private Long likeCount; // 좋아요 수
    private Boolean liked;  // 현재 로그인 유저가 누른 상태인지 여부 (readOne에서만 세팅됨)
}
