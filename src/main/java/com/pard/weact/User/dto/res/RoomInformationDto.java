package com.pard.weact.User.dto.res;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class RoomInformationDto {
    private String roomName;
    private String period; // 기간
    private int dayCountByWeek; // 주 회
    private int percent; // 달성율
}
