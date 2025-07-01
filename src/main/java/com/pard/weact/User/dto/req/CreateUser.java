package com.pard.weact.User.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUser {

    private String userName;

    private String gender;

    private String userId;

    private String pw;
}
