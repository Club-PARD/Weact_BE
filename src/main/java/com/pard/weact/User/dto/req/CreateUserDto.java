package com.pard.weact.User.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDto {

    private String userName;

    private String gender;

    private String userId;

    private String pw;
}
