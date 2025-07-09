package com.pard.weact.memberInformation.dto.res;

import lombok.*;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class HamburgerInfoDto {
    private String myName;
    private String myHabit;
    private String imageUrl;
    private int myPercent;
    private List<MemberNameAndHabitDto> memberNameAndHabitDtos;
}

