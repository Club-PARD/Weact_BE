package com.pard.weact.liked.repo;

import com.pard.weact.habitPost.entity.HabitPost;
import com.pard.weact.liked.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepo extends JpaRepository<Liked, Long> {

    Optional<Liked> findByUserIdAndHabitPost_Id(String userId, Long postId);


    long countByHabitPost_Id(Long postId); // habit_post_id 기준으로 카운트

    boolean existsByUserIdAndHabitPost_Id(String userId, Long postId);
}
