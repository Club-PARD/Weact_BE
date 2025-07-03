package com.pard.weact.habitPost.dto.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HabitPostSummaryDto {
    private String userName;     // 글쓴이
    private String message;      // 인증 메시지
    private String imageUrl;     // 이미지 경로
}
