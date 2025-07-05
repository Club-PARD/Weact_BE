package com.pard.weact.liked.entity;

import com.pard.weact.habitPost.entity.HabitPost;
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

    /** 좋아요 누른 사람의 ID (연관관계 없이 값만 저장) */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /** 좋아요가 눌린 게시물 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_post_id", nullable = false)
    private HabitPost habitPost;


}


