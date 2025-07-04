package com.pard.weact.room.dto.req;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class CreateRoomDto {
    private Long CreatorId;
    private List<Long> invitedIds; // 초대받는 사람들 id

    private String roomName;
    private Date startDate;
    private Date endDate;
    private String reward;
    private int dayCountByWeek;
}
