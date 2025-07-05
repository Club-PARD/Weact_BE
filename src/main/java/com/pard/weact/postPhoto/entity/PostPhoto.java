package com.pard.weact.postPhoto.entity;

import com.pard.weact.habitPost.entity.HabitPost;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image_files")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String original_name;   // 원본 파일명 (예: dog.jpg)
    private String unique_name;     // 서버에 저장된 파일명 (예: uuid_dog.jpg)
    private String path;           // 접근 가능한 전체 URL (예: http://localhost:8080/uuid_dog.jpg)

    @OneToOne(mappedBy = "photo")
    private HabitPost habitPost;

}