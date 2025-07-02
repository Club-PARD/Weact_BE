package com.pard.weact.habitPost.repo;

import com.pard.weact.habitPost.entity.HabitPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitPostRepo extends JpaRepository<HabitPost,Long> {
}
