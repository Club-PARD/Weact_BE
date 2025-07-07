package com.pard.weact.habitPost.entity;

import com.pard.weact.User.entity.User;
import com.pard.weact.comment.entity.Comment;
import com.pard.weact.liked.entity.Liked;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.postPhoto.entity.PostPhoto;

import com.pard.weact.room.entity.Room;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    // ✅ 직접적으로 user 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // ✅ 여전히 통계 용도로 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_information_id")
    private MemberInformation member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id")
    private PostPhoto photo;

    @OneToMany(mappedBy = "habitPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    private LocalDate date;
    private String message;
    private boolean isHaemyeong;

    @OneToMany(mappedBy = "habitPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Liked> likedList = new ArrayList<>();
}
