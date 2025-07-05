package com.pard.weact.habitPost.entity;

import com.pard.weact.liked.entity.Liked;
import com.pard.weact.postPhoto.entity.PostPhoto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private Boolean isHaemyeong;

    private LocalDate date; // 2025-07-03

    private String userId; // 로그인 할때의 아이디

//    @ManyToOne(fetch = FetchType.LAZY) // 달성률 계산 할때 쓰임
//    @JoinColumn(name = "member_information_id") // MemberInformation의 PK가 id라고 가정
//    private MemberInformation member;
//
//    // 속한 방 정보
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "room_id")
    private Long roomId;

//    // 인증글에 연결된 사진 (1:1)
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "post_photo_id")
    private Long photoId;

    // 좋아요 리스트 (1:N)
    @OneToMany(mappedBy = "habitPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Liked> likes = new ArrayList<>();


}
