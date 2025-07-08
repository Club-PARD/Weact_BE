package com.pard.weact.User.dto.res;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenAndUserDto {
    private String token;
    private AfterCreateUserDto user;
}

