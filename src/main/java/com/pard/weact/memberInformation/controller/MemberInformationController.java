package com.pard.weact.memberInformation.controller;

import com.pard.weact.memberInformation.dto.req.UpdateHabitAndRemindTimeDto;
import com.pard.weact.memberInformation.service.MemberInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberInformationController {
    private final MemberInformationService memberInformationService;

    @PatchMapping("")
    @Transactional
    public void updateHabitAndRemindTime(@RequestBody UpdateHabitAndRemindTimeDto updateHabitAndRemindTimeDto){
        memberInformationService.updateHabitAndRemindTime(updateHabitAndRemindTimeDto);
    }
}
