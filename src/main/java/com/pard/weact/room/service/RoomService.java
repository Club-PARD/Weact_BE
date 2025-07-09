package com.pard.weact.room.service;

import com.pard.weact.User.dto.res.NameAndPhotoDto;
import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.UserInvite.service.UserInviteService;
import com.pard.weact.inAppNotification.entity.NotificationType;
import com.pard.weact.inAppNotification.service.InAppNotificationService;
import com.pard.weact.memberInformation.dto.res.MemberInfosDto;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.memberInformation.repository.MemberInformationRepo;
import com.pard.weact.memberInformation.service.MemberInformationService;
import com.pard.weact.room.dto.req.CreateRoomDto;
import com.pard.weact.room.dto.res.AfterCreateRoomDto;
import com.pard.weact.room.dto.res.CelebrationDto;
import com.pard.weact.room.dto.res.CheckPointDto;
import com.pard.weact.room.dto.res.FinalRankingDto;
import com.pard.weact.room.entity.Room;
import com.pard.weact.room.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
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
    private final InAppNotificationService inAppNotificationService;

    public CheckPointDto getCheckPoint(Long roomId){ // 중간점검 랭킹 (1, 2등 리스트, 이름만, 공동가능)
        List<MemberInformation> firstRankMembers =  memberInformationRepo.findTopMembersByRoomId(roomId);
        List<MemberInformation> secondRankMembers = memberInformationRepo.findSecondPlaceMembersByRoomId(roomId);

        List<NameAndPhotoDto> firstRankNames = firstRankMembers.stream()
                .map(member -> NameAndPhotoDto.builder()
                        .userName(member.getUser().getUserName())
//                        .imageUrl(member.getUser().getProfilePhotoOrDefault())  메소드 미완
                        .build()
                ).toList();

        List<NameAndPhotoDto> secondRankNames = secondRankMembers.stream()
                .map(member -> NameAndPhotoDto.builder()
                        .userName(member.getUser().getUserName())
//                        .imageUrl(member.getUser().getProfilePhoto()) // 메소드 미완
                        .build()
                ).toList();

        return CheckPointDto.builder()
                .firstRanker(firstRankNames)
                .secondRanker(secondRankNames)
                .build();
    }

    public FinalRankingDto getFinalRanking(Long roomId, Long userId){ // / 최종랭킹 (3등까지 전체리스트, 공동가능, 습관, 달성율)
        List<MemberInformation> firstRankMembers = memberInformationRepo.findTopMembersByRoomId(roomId);
        List<MemberInformation> secondRankMembers = memberInformationRepo.findSecondPlaceMembersByRoomId(roomId);
        List<MemberInformation> thirdRankMembers = memberInformationRepo.findThirdPlaceMembersByRoomId(roomId);
        List<MemberInformation> allMembers = memberInformationRepo.findByRoomIdOrderByPercentDesc(roomId);
        User user = userRepo.findById(userId).orElseThrow();

        boolean amITop = firstRankMembers.stream()
                .anyMatch(member -> member.getUser().getId().equals(user.getId()));

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
                .amITop(amITop)
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

    public boolean checkDays(Long roomId){
        Room room = roomRepo.findById(roomId).orElseThrow();

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        List<DayOfWeek> activeDays = room.parseDays();

        return activeDays.contains(today);
    }

    public AfterCreateRoomDto createRoom(Long userId, CreateRoomDto createRoomDto){
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

        User creator = userRepo.findById(userId).orElseThrow();
        String creatorName = creator.getUserName();

        // 방 만들자마자 개최자는 member로 바로 넣기
        memberInformationService.createMemberInformation(creator, room);

        // 나머지 인원에 대해서는 초대장 만들기 (초대받은 사람들 id, 만든 사람 이름, room)
        userInviteService.createUserInvites(createRoomDto.getInvitedIds(), room ,creatorName);

        // 위에서 만든 초대장 보내기
        return AfterCreateRoomDto.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .dayCountByWeek(room.getDayCountByWeek())
                .creatorName(creatorName)
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
                doWorstCase(room);
            }
        }
    }

    public boolean checkThreeDays(Long roomId){
        Room room = roomRepo.findById(roomId).orElseThrow();

        LocalDate today = LocalDate.now();

        return toLocalDate(room.getStartDate()).plusDays(2).isAfter(today);
    }

    public CelebrationDto celebration(Long roomId){
        Room room = roomRepo.findById(roomId).orElseThrow();

        return CelebrationDto.builder()
                .roomName(room.getRoomName())
                .period(room.getPeriodFormatted())
//                .imageUrl() 1등 Default 사진 url 넣음 됨.
                .build();
    }

    // 아래로 보조 메소드
    public void doWorstCase(Room room){
        List<MemberInformation> memberInfos = memberInformationRepo.findByRoomId(room.getId());

        for(MemberInformation memberInformation : memberInfos){ // 멤버가 아무것도 안했을 때 조치.
            if(memberInformation.isDoNothing()){
                memberInformation.plusWorstCount(); // count 올리고
                notifyOthers(memberInformation.getUser(), room, NotificationType.WORST_CASE);
                // worstCase 모든 팀원에게 전달.

                if(memberInformation.checkCoin(room.getCoin())){ // 코인횟수 체크, 강퇴
                    notifyOthers(memberInformation.getUser(), room, NotificationType.OUT); // 나머지 인원들한테 알림.
                    memberInformationRepo.delete(memberInformation);
                    room.minusMemberCount();
                }
            }
        }
    }

    public void notifyOthers(User user, Room room, NotificationType type){
        // 무단결석한 user 제외하고 모두 구해서 알림전송.
        List<MemberInformation> memberInformations = memberInformationRepo.findAllByRoomIdAndUserIdNot(room.getId(), user.getId());

        for(MemberInformation memberInformation : memberInformations){
            inAppNotificationService.createNotification(type,
                    memberInformation.getUser().getUserName(),
                    memberInformation.getRoom().getRoomName(),
                    memberInformation.getUser().getId());
        }
    }

    public LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
