package com.pard.weact.room.dto.req;

import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class CreateRoomDto {
    private Long CreatorId;
    private List<Long> invitedIds; // 초대받는 사람들 id

    private String roomName;
    private Date startDate; // yyyy-MM-dd
    private Date endDate;
    private String reward;
    private String days; // 월화수목금
    private int dayCountByWeek; // 주 n회

    // Date → LocalDate 변환
    public LocalDate toLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    // 총 날짜 수 계산 (시작일 포함, 종료일 포함)
    public int dayCount() {
        if (startDate == null || endDate == null) return 0;

        LocalDate start = toLocalDate(startDate);
        LocalDate end = toLocalDate(endDate);
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }

    // 체크포인트 계산
    public List<LocalDate> checkPoints() { // 전체 일수의 20퍼센트마다 체크포인트로 설정.
        if (startDate == null || endDate == null) return List.of();

        LocalDate start = toLocalDate(startDate);
        LocalDate end = toLocalDate(endDate);
        int totalDays = (int) ChronoUnit.DAYS.between(start, end) + 1;

        int interval = Math.max(1, (int) Math.floor(totalDays * 0.2));
        List<LocalDate> checkpoints = new java.util.ArrayList<>();

        for (int i = interval; i < totalDays; i += interval) {
            // 체크포인트가 시작, 종료일과 겹치지 않을 때만 추가.
            if(!start.plusDays(i).isEqual(end) && !start.plusDays(i).isEqual(start)){
                checkpoints.add(start.plusDays(i));
            }
        }

        return checkpoints;
    }

    public int coinCount(int dayCount){ // 전체 일수의 10퍼센트 갯수 반환
        return  (int) (dayCount / 10.0);
    }
}
