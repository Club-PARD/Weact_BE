package com.pard.weact.room.service;

import com.pard.weact.User.dto.res.NameAndPhotoDto;
import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.UserInvite.service.UserInviteService;
import com.pard.weact.memberInformation.dto.res.MemberInfosDto;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.memberInformation.repository.MemberInformationRepo;
import com.pard.weact.memberInformation.service.MemberInformationService;
import com.pard.weact.room.dto.req.CreateRoomDto;
import com.pard.weact.room.dto.res.AfterCreateRoomDto;
import com.pard.weact.room.dto.res.CheckPointDto;
import com.pard.weact.room.dto.res.FinalRankingDto;
import com.pard.weact.room.entity.Room;
import com.pard.weact.room.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomRepo roomRepo;
    private final UserInviteService userInviteService;
    private final UserRepo userRepo;
    private final MemberInformationService memberInformationService;
    private final MemberInformationRepo memberInformationRepo;

    public CheckPointDto getCheckPoint(Long roomId){ // 중간점검 랭킹 (1, 2등 리스트, 이름만, 공동가능)
        List<MemberInformation> firstRankMembers =  memberInformationRepo.findTopMembersByRoomId(roomId);
        List<MemberInformation> secondRankMembers = memberInformationRepo.findSecondPlaceMembersByRoomId(roomId);

        List<NameAndPhotoDto> firstRankNames = firstRankMembers.stream()
                .map(member -> NameAndPhotoDto.builder()
                        .userName(member.getUser().getUserName())
                        .build()
                ).toList();

        List<NameAndPhotoDto> secondRankNames = secondRankMembers.stream()
                .map(member -> NameAndPhotoDto.builder()
                        .userName(member.getUser().getUserName())
                        .build()
                ).toList();

        return CheckPointDto.builder()
                .firstRanker(firstRankNames)
                .secondRanker(secondRankNames)
                .build();
    }

    public FinalRankingDto getFinalRanking(Long roomId){ // / 최종랭킹 (3등까지 전체리스트, 공동가능, 습관, 달성율)
        List<MemberInformation> firstRankMembers = memberInformationRepo.findTopMembersByRoomId(roomId);
        List<MemberInformation> secondRankMembers = memberInformationRepo.findSecondPlaceMembersByRoomId(roomId);
        List<MemberInformation> thirdRankMembers = memberInformationRepo.findThirdPlaceMembersByRoomId(roomId);
        List<MemberInformation> allMembers = memberInformationRepo.findByRoomIdOrderByPercentDesc(roomId);

        Set<Long> topIds = Stream.of(firstRankMembers, secondRankMembers, thirdRankMembers)
                .flatMap(List::stream)
                .map(m -> m.getUser().getId())
                .collect(Collectors.toSet());

        List<MemberInfosDto> restMembers = new ArrayList<>();
        AtomicInteger rank = new AtomicInteger(4);

        allMembers.stream()
                .filter(m -> !topIds.contains(m.getUser().getId()))
                .forEach(m -> restMembers.add(
                        MemberInfosDto.builder()
                                .userName(m.getUser().getUserName())
                                .habit(m.getHabit())
                                .percent(m.getPercent())
                                .build()
                ));

        return FinalRankingDto.builder()
                .firstPlace(toDto(firstRankMembers))
                .secondPlace(toDto(secondRankMembers))
                .thirdPlace(toDto(thirdRankMembers))
                .restMembers(restMembers)
                .build();
    }

    private List<MemberInfosDto> toDto(List<MemberInformation> list) { // DTO 헬퍼
        return list.stream()
                .map(m -> MemberInfosDto.builder()
                        .userName(m.getUser().getUserName())
                        .habit(m.getHabit())
                        .percent(m.getPercent())
                        .build())
                .toList();
    }


    public boolean checkOneDayCount(Long roomId){
        Room room = roomRepo.findById(roomId).orElseThrow();

        room.checkDateAndPlusOneDayCount();

        return room.getOneDayCount() == room.getMemberCount();
    }

    public AfterCreateRoomDto createRoom(CreateRoomDto createRoomDto){
        int dayCount =  createRoomDto.dayCount();

        Room room = Room.builder()
                .roomName(createRoomDto.getRoomName())
                .reward(createRoomDto.getReward())
                .startDate(createRoomDto.getStartDate())
                .endDate(createRoomDto.getEndDate())
                .days(createRoomDto.getDays())
                .dayCountByWeek(createRoomDto.getDayCountByWeek())
                .dayCount(dayCount)
                .coin(createRoomDto.coinCount(dayCount))
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
                .dayCountByWeek(room.getDayCountByWeek())
                .creatorName(creatorName)
                .userInviteIds(userInviteIds)
                .checkPoints(createRoomDto.checkPoints())
                .build();
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void checkByDayOfWeek() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DayOfWeek yesterdayDay = yesterday.getDayOfWeek();

        List<Room> rooms = roomRepo.findAll(); // 모든 Room 순회

        for (Room room : rooms) {
            List<DayOfWeek> activeDays = room.parseDays();

            LocalDate start = toLocalDate(room.getStartDate());
            LocalDate end = toLocalDate(room.getEndDate());

            if (yesterday.isBefore(start) || yesterday.isAfter(end)) {
                continue; // 어제가 활동 기간 밖이면 스킵
            }

            if (activeDays.contains(yesterdayDay)) {
                List<MemberInformation> memberInfos = memberInformationRepo.findByRoomId(room.getId());

                for(MemberInformation memberInformation : memberInfos){ // 멤버가 아무것도 안했을 때 조치.
                    if(memberInformation.isDoNothing()){
                        memberInformation.plusWorstCount();
                        if(memberInformation.checkCoin(room.getCoin())){
                            memberInformationRepo.delete(memberInformation);
                            room.minusMemberCount();
                        }
                    }
                }
            }
        }
    }

    public LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
