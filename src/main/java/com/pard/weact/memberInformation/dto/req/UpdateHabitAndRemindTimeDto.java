package com.pard.weact.memberInformation.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class UpdateHabitAndRemindTimeDto {
    private Long roomId;

    private String habit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String remindTime;
}
