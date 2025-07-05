package com.pard.weact.UserInvite.dto.req;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class UserInviteUpdateDto {
//    private Long userInviteId;
    private Long userId;
    private Long roomId;

    private int state;
}
