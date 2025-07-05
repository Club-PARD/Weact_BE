package com.pard.weact.postPhoto.repo;

import com.pard.weact.postPhoto.entity.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostPhotoRepo extends JpaRepository<PostPhoto,Long> {

}
