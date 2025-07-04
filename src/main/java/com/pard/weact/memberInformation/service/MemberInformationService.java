package com.pard.weact.memberInformation.service;

import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.memberInformation.dto.req.UpdateHabitAndRemindTimeDto;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.memberInformation.repository.MemberInformationRepo;
import com.pard.weact.room.entity.Room;
import com.pard.weact.room.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInformationService {
    private final MemberInformationRepo memberInformationRepo;
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;

    public void createMemberInformation(User user, Room room){
        memberInformationRepo.save(
                MemberInformation.builder()
                .user(user)
                .room(room)
                .build()
        );
    }

    public void updateHabitAndRemindTime(UpdateHabitAndRemindTimeDto updateHabitAndRemindTimeDto){
        MemberInformation memberInformation = memberInformationRepo.findByUserIdAndRoomId(updateHabitAndRemindTimeDto.getUserId(), updateHabitAndRemindTimeDto.getRoomId());
        memberInformation.update(updateHabitAndRemindTimeDto.getHabit(), updateHabitAndRemindTimeDto.getRemindTime());

        memberInformationRepo.save(memberInformation);
    }

    public void plusHabitCount(Long userId, Long roomId){
        MemberInformation memberInformation = memberInformationRepo.findByUserIdAndRoomId(userId, roomId);
        memberInformation.plusHabitCount();

        Room room = roomRepo.findById(roomId).orElseThrow();
        memberInformation.updateAndGetPercent(room.getDayCount());

        memberInformationRepo.save(memberInformation);
    }
}
