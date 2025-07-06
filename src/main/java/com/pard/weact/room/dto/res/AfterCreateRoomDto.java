package com.pard.weact.room.dto.res;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class AfterCreateRoomDto {
    private String creatorName;
    private List<Long> userInviteIds; // 초대장 id들
    private Long roomId;
    private String roomName;
    private int dayCountByWeek;
    private List<LocalDate> checkPoints;
}
