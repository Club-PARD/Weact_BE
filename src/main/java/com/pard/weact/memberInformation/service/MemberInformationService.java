package com.pard.weact.memberInformation.service;

import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.memberInformation.dto.req.CreateMemberInformationDto;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.memberInformation.repository.MemberInformationRepo;
import com.pard.weact.room.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInforamtionService {
    private final MemberInformationRepo memberInformationRepo;
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;

    public void createMemberInformation(Long userId, Long roomId){
        memberInformationRepo.save(
                MemberInformation.builder()
                .user(userRepo.findById(userId).orElseThrow())
                .room(roomRepo.findById(roomId).orElseThrow())
                .build()
        );
    }
}
