package com.pard.weact.UserInvite.entity;

import com.pard.weact.User.entity.User;
import com.pard.weact.room.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Builder
public class UserInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 초대받은 유저 정보
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 초대가 온 방 정보
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    // 초대 정보
    private int state;
    private String sender;

    public void updateState(int state){
        this.state = state;
    }
}
