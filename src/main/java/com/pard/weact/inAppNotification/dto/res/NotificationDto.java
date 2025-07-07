package com.pard.weact.inAppNotification.dto.res;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class NotificationDto {
    private String senderName;
    private String roomName;
    private String message;
    private String type;
}
