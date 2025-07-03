package com.pard.weact.habitPost.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String message;

    private Boolean isHaemyeong;

    private LocalDate date; // 2025-07-03

    private String userId; // 적은 사람 id

    @Column(nullable = true)
    private Long photoId;

    private Long roomId;


}
