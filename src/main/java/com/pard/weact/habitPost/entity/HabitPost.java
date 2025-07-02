package com.pard.weact.habitPost.entity;


import com.pard.weact.postPhoto.entity.PostPhoto;
import jakarta.persistence.*;

public class HabitPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = true)
    private Boolean isHaemyeong;

    // 어떤 방의 인증인지
//    @ManyToOne
//    @JoinColumn(name = "room_id")
    private HabitRoom habitRoom;

    // 어떤 사용자가 작성했는지
//    @ManyToOne
//    @JoinColumn(name = "member_id")
    private MemberInformation memberInformation;

    // 어떤 사진을 첨부했는지 (PostPhoto와 연결)
    @OneToOne
    @JoinColumn(name = "photo_id")
    private PostPhoto photo;

    @OneToOne
    @JoinColumn(name = "accept_response_id")
    private AcceptResponse acceptResponse;

}
