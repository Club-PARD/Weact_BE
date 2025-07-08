package com.pard.weact.room.dto.res;

import com.pard.weact.memberInformation.dto.res.MemberInfosDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class FinalRankingDto {
    private List<MemberInfosDto> firstPlace;
    private List<MemberInfosDto> secondPlace;
    private List<MemberInfosDto> thirdPlace;
    private List<MemberInfosDto> restMembers;

    private boolean amITop;
}
