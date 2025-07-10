package com.pard.weact.room.entity;

import com.pard.weact.UserInvite.entity.UserInvite;
import com.pard.weact.memberInformation.entity.MemberInformation;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInvite> invites;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberInformation> memberInfos;

    private String roomName;
    private int memberCount = 0;

    private Date startDate; // yyyy-MM-dd
    private Date endDate;

    private String reward;
    private int coin; // 코인 다 까이면 강퇴.

    private String days; // ex) 월화수목금
    private int dayCount; // 전체 날 수
    private int dayCountByWeek; // 주 n회
    private int oneDayCount; // 하루에 몇번 인증되었는지

    private LocalDate lastUpdatedDate; // 마지막으로 초기화된 날짜

    // 인증 전에 가장 마지막 인증일자가 오늘 날짜와 같은지 확인, 아니면 리셋.
    public void checkDateAndPlusOneDayCount() {
        LocalDate today = LocalDate.now();
        if (lastUpdatedDate == null || !lastUpdatedDate.isEqual(today)) {
            this.oneDayCount = 0;
            this.lastUpdatedDate = today;
        }
        oneDayCount ++;
    }

    public void plusMemberCount(){
        this.memberCount ++;
    }

    public void minusMemberCount(){
        this.memberCount --;
    }

    public List<DayOfWeek> parseDays() {
        Map<String, DayOfWeek> dayMap = Map.of(
                "월", DayOfWeek.MONDAY,
                "화", DayOfWeek.TUESDAY,
                "수", DayOfWeek.WEDNESDAY,
                "목", DayOfWeek.THURSDAY,
                "금", DayOfWeek.FRIDAY,
                "토", DayOfWeek.SATURDAY,
                "일", DayOfWeek.SUNDAY
        );

        List<DayOfWeek> result = new ArrayList<>();
        for (char ch : days.toCharArray()) {
            String day = String.valueOf(ch);
            if (dayMap.containsKey(day)) {
                result.add(dayMap.get(day));
            }
        }
        return result;
    }

    public String getPeriodFormatted() {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M.d");
        return start.format(formatter) + " - " + end.format(formatter);
    }

    public String daysFormatted(){
        String[] chars = this.days.split("");  // 각 글자를 분리

        return String.join(",", chars);  // 쉼표로 join
    }
}

