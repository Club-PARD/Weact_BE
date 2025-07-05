package com.pard.weact.User.dto.res;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class AfterCreateUserDto {
    private Long id;
    private String userId;
}
