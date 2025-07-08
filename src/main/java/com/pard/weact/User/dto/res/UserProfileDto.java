package com.pard.weact.User.dto.res;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private String userName;
    private String profilePhoto; // 실제 URL (기본 or 사용자가 설정한 것)
}
