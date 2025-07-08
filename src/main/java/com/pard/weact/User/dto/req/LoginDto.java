package com.pard.weact.User.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    private String userId;
    private String password;
}