package com.pard.weact.User.dto.res;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class AddUserDto {
    private Long id;
    private String userId;
    private String imageUrl;
}

