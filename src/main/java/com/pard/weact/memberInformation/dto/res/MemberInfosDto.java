package com.pard.weact.memberInformation.dto.res;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class MemberInfosDto {
    private String userName;
    private String habit;
    private int percent;
}
