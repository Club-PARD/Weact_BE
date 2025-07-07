package com.pard.weact.liked.service;


import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.habitPost.repo.HabitPostRepo;
import com.pard.weact.liked.dto.res.LikeToggleDto;
import com.pard.weact.liked.entity.Liked;
import com.pard.weact.liked.repo.LikedRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LikedService {

    private final LikedRepo likedRepo;
    private final HabitPostRepo habitPostRepo;
    private final UserRepo userRepo;

    public LikeToggleDto toggleLike(Long habitPostId, Long userId) {
        Liked liked = likedRepo.findByHabitPostIdAndUserId(habitPostId, userId)
                .orElseGet(() -> Liked.builder()
                        .habitPost(habitPostRepo.findById(habitPostId).orElseThrow())
                        .user(userRepo.findById(userId).orElseThrow())
                        .liked(false)
                        .build());

        if (liked.isLiked()) {
            liked.unlike();
        } else {
            liked.like();
        }
        likedRepo.save(liked);

        long likeCount = likedRepo.countByHabitPostIdAndLikedIsTrue(habitPostId);
        return new LikeToggleDto(liked.isLiked(), likeCount);
    }

    public boolean isLiked(Long postId, Long userId) {
        return likedRepo.findByHabitPostIdAndUserId(postId, userId)
                .map(Liked::isLiked)
                .orElse(false);
    }

    public Long countLikes(Long postId) {
        return likedRepo.countByHabitPostIdAndLikedIsTrue(postId);
    }

}

