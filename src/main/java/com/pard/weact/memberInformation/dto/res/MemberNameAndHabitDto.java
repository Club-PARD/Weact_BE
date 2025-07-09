package com.pard.weact.memberInformation.dto.res;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class MemberNameAndHabitDto {
    private String memberName;
    private String memberHabit;
    private String imageUrl;
}
