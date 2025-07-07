package com.pard.weact.habitPost.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Schema(description = "습관 인증글 요청 DTO")
public class CreateHabitPostDto {
    @Schema(description = "유저 ID", example = "2")
    private Long userId;

    @Schema(description = "룸 ID", example = "1")
    private Long roomId;

    @Schema(description = "메시지", example = "오늘도 인증 성공!")
    private String message;

    @Schema(description = "해명 여부", example = "false")
    private Boolean isHaemyeong;
}
