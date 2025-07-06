package com.pard.weact.room.dto.res;

import com.pard.weact.User.dto.res.NameAndPhotoDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class CheckPointDto {
    private List<NameAndPhotoDto> firstRanker;
    private List<NameAndPhotoDto> secondRanker;
}
