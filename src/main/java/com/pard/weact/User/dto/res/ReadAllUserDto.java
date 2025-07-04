package com.pard.weact.User.dto.res;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReadAllUserDto {
    private Long id;

    private String userName;

    private String gender;

    private String userId;
}
