package com.pard.weact.room.dto.res;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class AfterCreateRoomDto {
    private String creatorName;
    private Long roomId;
}
