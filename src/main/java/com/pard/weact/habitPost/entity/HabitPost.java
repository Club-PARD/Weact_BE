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

    private LocalDate date;

    private String userId;

    private Long roomId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_photo_id")
    private PostPhoto photo;

    @OneToMany(mappedBy = "habitPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Liked> likes = new ArrayList<>();
}
