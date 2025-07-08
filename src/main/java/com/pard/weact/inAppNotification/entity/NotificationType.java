package com.pard.weact.inAppNotification.entity;

import lombok.Getter;

@Getter
public enum NotificationType{
    INVITE("님이 보낸 그룹 초대"),
    WORST_CASE("님이 오늘 인증하지 않았어요"),
    REMIND("님 오늘의 습관을 인증해 주세요!"),
    OUT(" 반복된 무단 미인증으로 퇴장되었어요");


    private final String message;

    NotificationType(String message){
        this.message = message;
    }
}
