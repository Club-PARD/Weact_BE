package com.pard.weact.User.dto.res;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class HomeScreenDto {
    private int month;
    private int day;
    private List<RoomInformationDto> roomInformationDtos;
}
