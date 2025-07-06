package com.pard.weact.liked.repo;

import com.pard.weact.liked.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedRepo extends JpaRepository<Liked, Long> {
    Optional<Liked> findByHabitPostIdAndMemberId(Long habitPostId, Long memberId);
    Long countByHabitPostIdAndLikedIsTrue(Long habitPostId);
}


