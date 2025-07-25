package com.pard.weact.inAppNotification.service;

import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.inAppNotification.dto.res.NotificationDto;
import com.pard.weact.inAppNotification.entity.InAppNotification;
import com.pard.weact.inAppNotification.entity.NotificationType;
import com.pard.weact.inAppNotification.repository.InAppNotificationRepo;
import com.pard.weact.room.entity.Room;
import com.pard.weact.room.repository.RoomRepo;
import com.pard.weact.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InAppNotificationService {
    private final InAppNotificationRepo inAppNotificationRepo;
    private final RoomRepo roomRepo;
    private final UserRepo userRepo;

    public List<NotificationDto> getNotification(Long userId){
        User user = userRepo.findById(userId).orElseThrow();

        List<InAppNotification> inAppNotifications =  inAppNotificationRepo.findAllByTargetId(userId);

        return inAppNotifications.stream().map(
                inAppNotification -> NotificationDto
                        .builder()
                        .roomName(inAppNotification.getRoomName())
                        .message(inAppNotification.getMessage())
                        .type(inAppNotification.getType().toString())
                        .roomId(inAppNotification.getRoomId())
                        .userInviteId(inAppNotification.getUserInviteId())
                        .build()
        ).toList();
    }


    // 초대장 만들 때는 초대장 id 필요함 -> 오버라이딩
    public void createNotification(NotificationType type, String name, Room room, Long targetId, Long userInviteId){
        String message = name + type.getMessage();

        InAppNotification inAppNotification = InAppNotification.builder()
                .roomName(room.getRoomName())
                .message(message)
                .type(type)
                .roomId(room.getId())
                .targetId(targetId)
                .userInviteId(userInviteId)
                .build();

        inAppNotificationRepo.save(inAppNotification);
    }

    public void createNotification(NotificationType type, String name, Room room, Long targetId){
        String message = name + type.getMessage();

        InAppNotification inAppNotification = InAppNotification.builder()
                .roomName(room.getRoomName())
                .message(message)
                .type(type)
                .roomId(room.getId())
                .targetId(targetId)
                .build();

        inAppNotificationRepo.save(inAppNotification);
    }
}
