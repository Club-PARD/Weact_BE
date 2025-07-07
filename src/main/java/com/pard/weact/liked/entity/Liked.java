package com.pard.weact.liked.entity;

import com.pard.weact.habitPost.entity.HabitPost;
import com.pard.weact.memberInformation.entity.MemberInformation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Liked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_post_id")
    private HabitPost habitPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberInformation member;

    private boolean liked;

    public void like() {
        this.liked = true;
    }

    public void unlike() {
        this.liked = false;
    }
}


