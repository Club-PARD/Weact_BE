package com.pard.weact.defaultImage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "default_images")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String uniqueName;

    private String path; // S3 URL
}

