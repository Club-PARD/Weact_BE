package com.pard.weact.UserInvite.controller;

import com.pard.weact.User.entity.User;
import com.pard.weact.UserInvite.dto.req.UserInviteUpdateDto;
import com.pard.weact.UserInvite.service.UserInviteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "jwtAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/invite")
public class UserInviteController {
    private final UserInviteService userInviteService;

    @PatchMapping("")
    @Transactional
    public void responseUpdate(@AuthenticationPrincipal User user, @RequestBody UserInviteUpdateDto userInviteUpdateDto){
        userInviteService.responseUpdate(user.getId(), userInviteUpdateDto);
    }
}
