package com.pard.weact.room.service;

import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.UserInvite.service.UserInviteService;
import com.pard.weact.memberInformation.service.MemberInformationService;
import com.pard.weact.room.dto.req.CreateRoomDto;
import com.pard.weact.room.dto.res.AfterCreateRoomDto;
import com.pard.weact.room.entity.Room;
import com.pard.weact.room.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomRepo roomRepo;
    private final UserInviteService userInviteService;
    private final UserRepo userRepo;
    private final MemberInformationService memberInformationService;


    public AfterCreateRoomDto createRoom(CreateRoomDto createRoomDto){

        Room room = Room.builder()
                .roomName(createRoomDto.getRoomName())
                .reward(createRoomDto.getReward())
                .startDate(createRoomDto.getStartDate())
                .endDate(createRoomDto.getEndDate())
                .dayCountByWeek(createRoomDto.getDayCountByWeek())
                .build();

        roomRepo.save(room);

        User creator = userRepo.findById(createRoomDto.getCreatorId()).orElseThrow();
        String creatorName = creator.getUserName();

        // 방 만들자마자 개최자는 member로 바로 넣기
        memberInformationService.createMemberInformation(creator, room);

        // 나머지 인원에 대해서는 초대장 만들기 (초대받은 사람들 id, 만든 사람 이름, room)
        List<Long> userInviteIds = userInviteService.createUserInvites(createRoomDto.getInvitedIds(), room ,creatorName);

        // 위에서 만든 초대장 보내기
        return AfterCreateRoomDto.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .creatorName(creatorName)
                .userInviteIds(userInviteIds)
                .build();
    }
}
