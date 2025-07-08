package com.pard.weact.memberInformation.controller;

import com.pard.weact.memberInformation.dto.req.UpdateHabitAndRemindTimeDto;
import com.pard.weact.memberInformation.dto.res.HamburgerInfoDto;
import com.pard.weact.memberInformation.service.MemberInformationService;
import com.pard.weact.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberInformationController {
    private final MemberInformationService memberInformationService;
    private final RoomService roomService;

    @GetMapping("/hamburger/{userId}/{roomId}")
    public HamburgerInfoDto showHamburgerBar(@PathVariable Long userId, @PathVariable Long roomId){
        return memberInformationService.showHamburgerBar(userId, roomId);
    }

    @PatchMapping("/habitAndRemindTime")
    @Transactional
    public void updateHabitAndRemindTime(@RequestBody UpdateHabitAndRemindTimeDto updateHabitAndRemindTimeDto){
        memberInformationService.updateHabitAndRemindTime(updateHabitAndRemindTimeDto);
    }

}
