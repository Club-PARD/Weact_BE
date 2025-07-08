package com.pard.weact.inAppNotification.controller;

import com.pard.weact.inAppNotification.dto.res.NotificationDto;
import com.pard.weact.inAppNotification.service.InAppNotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "jwtAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/inAppNotification")
public class InAppNotificationController {
    private final InAppNotificationService inAppNotificationService;

    @GetMapping("/getNotification/{userId}")
    public List<NotificationDto> getNotification(@PathVariable Long userId){
        return inAppNotificationService.getNotification(userId);
    }
}
