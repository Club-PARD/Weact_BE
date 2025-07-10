package com.pard.weact.UserInvite.service;

import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.UserInvite.dto.req.UserInviteUpdateDto;
import com.pard.weact.UserInvite.entity.UserInvite;
import com.pard.weact.UserInvite.repository.UserInviteRepo;
import com.pard.weact.inAppNotification.entity.NotificationType;
import com.pard.weact.inAppNotification.service.InAppNotificationService;
import com.pard.weact.memberInformation.service.MemberInformationService;
import com.pard.weact.room.entity.Room;
import com.pard.weact.room.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInviteService {
    private final UserInviteRepo userInviteRepo;
    private final MemberInformationService memberInformationService;
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;
    private final InAppNotificationService inAppNotificationService;

    public void createUserInvites(List<Long> invitedIds, Room room, String creatorName){
        // 초대받은 사람들 id로 users 찾기
        List<User> users = userRepo.findAllById(invitedIds);

        // 그대로 UserInvite에 하나씩 넣어주기
        List<UserInvite> userInvites = users.stream()
                .map(user -> UserInvite.builder()
                        .user(user)
                        .room(room)
                        .state(0)
                        .sender(creatorName)
                        .build())
                .toList();

        for(User user : users){
            // 초대 받는 사람 전부 target 만들어야 됨.
            inAppNotificationService.createNotification(NotificationType.INVITE, user.getUserName(), room, user.getId());
        }

        userInviteRepo.saveAll(userInvites);
    }

    public void responseUpdate(Long userId, UserInviteUpdateDto userInviteUpdateDto){
        User user = userRepo.findById(userId).orElseThrow();
        Room room = roomRepo.findById(userInviteUpdateDto.getRoomId()).orElseThrow();

        UserInvite userInvite = userInviteRepo.findByUserIdAndRoomId(userId, userInviteUpdateDto.getRoomId());
        userInvite.updateState(userInviteUpdateDto.getState());

        if(userInvite.getState() == 1){
            memberInformationService.createMemberInformation(userInvite.getUser(), userInvite.getRoom());
        }

        userInviteRepo.save(userInvite);
    }
}
