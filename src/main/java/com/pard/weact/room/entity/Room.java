package com.pard.weact.room.entity;

import com.pard.weact.UserInvite.entity.UserInvite;
import com.pard.weact.memberInformation.entity.MemberInformation;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

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

    private Date startDate;
    private Date endDate;

    private String reward;
    private int coin; // 코인 다 까이면 강퇴.

    private int dayCount; // 전체 날 수
    private int dayCountByWeek; // 주 n회

    public void plusMemberCount(){
        this.memberCount ++;
    }
}

