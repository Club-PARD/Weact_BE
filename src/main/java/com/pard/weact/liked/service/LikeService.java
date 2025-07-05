package com.pard.weact.liked.service;

import com.pard.weact.habitPost.repo.HabitPostRepo;
import com.pard.weact.habitPost.entity.HabitPost;
import com.pard.weact.liked.dto.res.LikeToggleDto;
import com.pard.weact.liked.entity.Liked;
import com.pard.weact.liked.repo.LikeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepo likeRepo;
    private final HabitPostRepo habitPostRepo;

    @Transactional
    public LikeToggleDto toggleLike(String userId, Long postId) {

        Optional<Liked> existing = likeRepo.findByUserIdAndHabitPost_Id(userId, postId);


        boolean liked;
        if (existing.isEmpty()) {
            HabitPost post = habitPostRepo.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

            Liked newLike = Liked.builder()
                        .userId(userId)
                        .habitPost(post)
                        .build();
            likeRepo.save(newLike);
            liked = true;
        } else {
            likeRepo.delete(existing.get());
                liked = false;
        }

        long likeCount = likeRepo.countByHabitPost_Id(postId);

        return LikeToggleDto.builder()
                .liked(liked)
                .likeCount(likeCount)
                .build();
    }

    public long countLikes(Long postId) {
        return likeRepo.countByHabitPost_Id(postId);
    }

    public boolean isLiked(String userId, Long postId) {
        return likeRepo.existsByUserIdAndHabitPost_Id(userId, postId);
    }

}

