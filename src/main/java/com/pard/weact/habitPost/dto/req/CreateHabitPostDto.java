package com.pard.weact.habitPost.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateHabitPostDto {
    private Long userId;
    private Long roomId;
    private String message;
    private Boolean isHaemyeong;
}
