package com.pard.weact.room.dto.res;

import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class AfterCreateRoomDto {
    private Long creatorId;
    private String creatorName;

    private List<Long> userInviteIds;

    private Long roomId;
    private String roomName;
}
