package com.pard.weact.habitPost.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateHabitPostDto {
    private Long userId;
    private Long photoId;
    private Long roomId;
    private LocalDate date;
    private String message;
    private Boolean isHaemyeong;
}
