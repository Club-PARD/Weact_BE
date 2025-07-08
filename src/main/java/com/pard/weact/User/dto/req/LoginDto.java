package com.pard.weact.User.dto.req;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class LoginDto {
    private String userId;
    private String password;
}
