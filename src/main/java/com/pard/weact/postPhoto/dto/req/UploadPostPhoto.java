package com.pard.weact.postPhoto.dto.req;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadPostPhoto {


    private MultipartFile image; // 인증 사진
}
