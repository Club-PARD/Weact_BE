package com.pard.weact.User.dto.res;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class NameAndPhotoDto {
    private String userName;

    // 프로필사진 필드
    private String imageUrl;
}
