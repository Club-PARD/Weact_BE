package com.pard.weact.liked.service;

import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.habitPost.repo.HabitPostRepo;
import com.pard.weact.liked.entity.Liked;
import com.pard.weact.liked.repo.LikedRepo;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.memberInformation.repository.MemberInformationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikedService {

    private final LikedRepo likedRepo;
    private final HabitPostRepo habitPostRepo;
    private final MemberInformationRepo memberInformationRepo;

    public void like(Long habitPostId, Long memberId) {
        Liked liked = likedRepo.findByHabitPostIdAndMemberId(habitPostId, memberId)
                .orElseGet(() -> Liked.builder()
                        .habitPost(habitPostRepo.findById(habitPostId).orElseThrow())
                        .member(memberInformationRepo.findById(memberId).orElseThrow())
                        .build());
        liked.like();
        likedRepo.save(liked);
    }

    public void unlike(Long habitPostId, Long memberId) {
        Optional<Liked> likedOpt = likedRepo.findByHabitPostIdAndMemberId(habitPostId, memberId);
        likedOpt.ifPresent(liked -> {
            liked.unlike();
            likedRepo.save(liked);
        });
    }
    public boolean isLiked(Long postId, Long memberId) {
        return likedRepo.findByHabitPostIdAndMemberId(postId, memberId)
                .map(Liked::isLiked)
                .orElse(false);
    }
    public Long countLikes(Long postId) {
        return likedRepo.countByHabitPostIdAndLikedIsTrue(postId);
    }


}

