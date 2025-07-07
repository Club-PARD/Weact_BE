package com.pard.weact.inAppNotification.entity;

import lombok.Getter;

@Getter
public enum NotificationType{
    INVITE("님이 보낸 그룹 초대"),
    WORST_CASE("님이 오늘 인증하지 않았어요"),
    REMIND("님 오늘은 안하시나요?");

    private final String message;

    NotificationType(String message){
        this.message = message;
    }
}
