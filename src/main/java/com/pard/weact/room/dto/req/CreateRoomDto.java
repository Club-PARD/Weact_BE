package com.pard.weact.room.dto.req;

import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class CreateRoom {
    private Long CreatorId;
    private List<Long> invitedIds;

    private String roomName;
    private int startDate;
    private int endDate;
    private String reward;
    private int date;
}
