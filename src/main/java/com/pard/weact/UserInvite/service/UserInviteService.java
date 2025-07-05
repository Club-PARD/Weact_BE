package com.pard.weact.UserInvite.service;

import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.UserInvite.dto.req.UserInviteUpdateDto;
import com.pard.weact.UserInvite.entity.UserInvite;
import com.pard.weact.UserInvite.repository.UserInviteRepo;
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

    public List<Long> createUserInvites(List<Long> invitedIds, Room room, String creatorName){
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

        userInviteRepo.saveAll(userInvites);

        return userInvites.stream().map(
                UserInvite::getId
        ).toList();
    }

    public void responseUpdate(UserInviteUpdateDto userInviteUpdateDto){
        User user = userRepo.findById(userInviteUpdateDto.getUserId()).orElseThrow();
        Room room = roomRepo.findById(userInviteUpdateDto.getRoomId()).orElseThrow();

        UserInvite userInvite = userInviteRepo.findByUserIdAndRoomId(userInviteUpdateDto.getUserId(), userInviteUpdateDto.getRoomId());
        userInvite.updateState(userInviteUpdateDto.getState());

        if(userInvite.getState() == 1){
            memberInformationService.createMemberInformation(userInvite.getUser(), userInvite.getRoom());
        }

        userInviteRepo.save(userInvite);
    }
}
