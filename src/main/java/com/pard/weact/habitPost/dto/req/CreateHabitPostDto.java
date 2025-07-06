package com.pard.weact.habitPost.dto.req;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateHabitPostDto {
    private Long memberId;
    private String message;
    private Long roomId;
    private Boolean isHaemyeong;
}
