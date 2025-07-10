package com.pard.weact.room.dto.res;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class RoomInfoDto {
    private String roomName;
    private int dayCountByWeek;
    private List<LocalDate> checkPoints;
    private String reward;
    private String period;
    private String days;
}
