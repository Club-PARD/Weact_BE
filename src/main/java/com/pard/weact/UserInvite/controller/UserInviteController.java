package com.pard.weact.UserInvite.controller;

import com.pard.weact.UserInvite.dto.req.UserInviteUpdateDto;
import com.pard.weact.UserInvite.service.UserInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invite")
public class UserInviteController {
    private final UserInviteService userInviteService;

    @PatchMapping("")
    @Transactional
    public void responseUpdate(@RequestBody UserInviteUpdateDto userInviteUpdateDto){
        userInviteService.responseUpdate(userInviteUpdateDto);
    }
}
