package com.pard.weact.memberInformation.entity;

import com.pard.weact.User.entity.User;
import com.pard.weact.habitPost.entity.HabitPost;
import com.pard.weact.room.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class MemberInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_information_id")
    private Long id;

    // 방에 속해있는 유저 정보
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "memberInformation", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<HabitPost> habitPosts = new ArrayList<>();

    // 방 정보
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    // 유저의 개인 필드
    private int habitCount = 0;
    private int worstCount = 0;
    private String habit;
    private LocalTime remindTime;
    private int percent; // 달성율
    private boolean isDoNothing = true; // 아무것도 안 올렸는지

    public void update(String habit, LocalTime remindTime){
        this.habit = habit;
        this.remindTime = remindTime;
    }

    public int updateAndGetPercent(int dayCount){
        return this.percent = (int) (habitCount / (float) dayCount * 100);
    }

    public void plusHabitCount(){
        habitCount ++;
    }

    public void plusWorstCount(){
        worstCount ++;
    }

    public boolean checkCoin(int coin){
        return this.worstCount == coin;
    }

    public void updateDoNothing(){
        this.isDoNothing = false;
    }
}
