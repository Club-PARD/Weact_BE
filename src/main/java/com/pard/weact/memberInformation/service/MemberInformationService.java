package com.pard.weact.memberInformation.service;

import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.memberInformation.dto.req.UpdateHabitAndRemindTimeDto;
import com.pard.weact.memberInformation.dto.res.HamburgerInfoDto;
import com.pard.weact.memberInformation.dto.res.MemberNameAndHabitDto;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.memberInformation.repository.MemberInformationRepo;
import com.pard.weact.room.entity.Room;
import com.pard.weact.room.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        room.plusMemberCount();
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

    public String plusWorstCount(Long userId, Long roomId){
        MemberInformation memberInformation = memberInformationRepo.findByUserIdAndRoomId(userId, roomId);
        memberInformation.plusWorstCount();

        Room room = roomRepo.findById(roomId).orElseThrow();

    }

    public HamburgerInfoDto showHamburgerBar(Long userId, Long roomId){
        User user = userRepo.findById(userId).orElseThrow();
        MemberInformation memberMe = memberInformationRepo.findByUserIdAndRoomId(userId, roomId);
        List<MemberInformation> memberInfos =  memberInformationRepo.findAllByRoomIdAndUserIdNot(roomId, userId);

        List<MemberNameAndHabitDto> memberNameAndHabitDtos = memberInfos.stream().map(
                memberInfo -> MemberNameAndHabitDto.builder()
                        .memberName(memberInfo.getUser().getUserName())
                        .memberHabit(memberInfo.getHabit())
                        .build()
        ).toList();

        return HamburgerInfoDto.builder()
                .myName(user.getUserName())
                .myHabit(memberMe.getHabit())
                .myPercent(memberMe.getPercent())
                .memberNameAndHabitDtos(memberNameAndHabitDtos)
                .build();
    }
}
