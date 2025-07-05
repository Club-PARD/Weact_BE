package com.pard.weact.habitPost.repo;

import com.pard.weact.habitPost.entity.HabitPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitPostRepo extends JpaRepository<HabitPost,Long> {
    List<HabitPost> findAllByRoomIdAndDate(Long roomId,LocalDate date);

}
