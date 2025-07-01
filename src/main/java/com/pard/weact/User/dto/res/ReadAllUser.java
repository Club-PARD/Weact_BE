package com.pard.weact.User.dto.res;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReadAllUser {
    private Long id;

    private String userName;

    private String gender;

    private String userId;

}
