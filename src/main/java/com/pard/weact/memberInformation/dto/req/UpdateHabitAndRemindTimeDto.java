package com.pard.weact.memberInformation.dto.req;

import lombok.*;

import java.util.Date;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class UpdateHabitAndRemindTimeDto {
    private Long userId;
    private Long roomId;

    private String habit;
    private Date remindTime;
}
