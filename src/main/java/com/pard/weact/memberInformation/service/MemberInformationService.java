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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        String[] parts = updateHabitAndRemindTimeDto.getRemindTime().trim().split(" ");

        String ampm = parts[0];     // "오전" or "오후"
        String timePart = parts[1]; // "06:00"

        // "06:00" -> LocalTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.parse(timePart, formatter);

        if ("오후".equals(ampm) && time.getHour() < 12) {
            time = time.plusHours(12);
        } else if ("오전".equals(ampm) && time.getHour() == 12) {
            // 오전 12시는 자정 → 00시로 바꿔야 함
            time = time.minusHours(12);
        }

        memberInformation.update(updateHabitAndRemindTimeDto.getHabit(), time);

        memberInformationRepo.save(memberInformation);
    }

    public void plusHabitCount(Long userId, Long roomId){
        MemberInformation memberInformation = memberInformationRepo.findByUserIdAndRoomId(userId, roomId);
        memberInformation.plusHabitCount();

        Room room = roomRepo.findById(roomId).orElseThrow();
        memberInformation.updateAndGetPercent(room.getDayCount());

        memberInformationRepo.save(memberInformation);
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
