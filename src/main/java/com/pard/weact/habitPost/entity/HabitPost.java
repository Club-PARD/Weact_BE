package com.pard.weact.habitPost.entity;


import com.pard.weact.postPhoto.entity.PostPhoto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = true)
    private Boolean isHaemyeong;

    private LocalDate date;

    private Long userId;

    @Column(nullable = true)
    private Long photoId;

    private Long roomId;
    private Long acceptResponseId; // 처음 수락한 유저 ID (nullable)
}
