package com.pard.weact.defaultImage.repo;

import com.pard.weact.defaultImage.entity.DefaultImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultImageRepo extends JpaRepository<DefaultImage, Long> {
}