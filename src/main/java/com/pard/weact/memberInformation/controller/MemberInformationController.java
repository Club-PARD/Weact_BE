package com.pard.weact.memberInformation.controller;

import com.pard.weact.User.entity.User;
import com.pard.weact.memberInformation.dto.req.UpdateHabitAndRemindTimeDto;
import com.pard.weact.memberInformation.dto.res.HamburgerInfoDto;
import com.pard.weact.memberInformation.service.MemberInformationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "jwtAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberInformationController {
    private final MemberInformationService memberInformationService;

    @GetMapping("/hamburger/{roomId}")
    public HamburgerInfoDto showHamburgerBar(@AuthenticationPrincipal User user, @PathVariable Long roomId){
        return memberInformationService.showHamburgerBar(user.getId(), roomId);
    }

    @PatchMapping("/habitAndRemindTime")
    @Transactional
    public void updateHabitAndRemindTime(@AuthenticationPrincipal User user, @RequestBody UpdateHabitAndRemindTimeDto updateHabitAndRemindTimeDto){
        memberInformationService.updateHabitAndRemindTime(user.getId(), updateHabitAndRemindTimeDto);
    }
}
