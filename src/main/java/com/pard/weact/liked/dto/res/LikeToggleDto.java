package com.pard.weact.liked.dto.res;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeToggleDto {
    private boolean liked;
    private long likeCount;
}


