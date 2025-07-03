package com.pard.weact.habitPost.dto.req;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadPhotoDto {

    private MultipartFile photo; // 인증 사진
}
