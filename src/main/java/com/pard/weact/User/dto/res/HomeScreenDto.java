package com.pard.weact.User.dto.res;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class HomeScreenDto {
    private Date today;
    private List<RoomInformationDto> roomInformationDtos;
}
