package com.pard.weact.room.dto.res;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class CelebrationDto {
    private String roomName;
    private String period;
    private String imageUrl;
}
