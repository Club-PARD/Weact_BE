package com.pard.weact.habitPost.service;

import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.User.service.UserService;
import com.pard.weact.habitPost.dto.req.CreateHabitPostDto;
import com.pard.weact.habitPost.dto.req.UploadPhotoDto;
import com.pard.weact.habitPost.dto.res.PostResultListDto;
import com.pard.weact.habitPost.dto.res.PostResultOneDto;
import com.pard.weact.habitPost.entity.HabitPost;
import com.pard.weact.habitPost.repo.HabitPostRepo;
import com.pard.weact.liked.service.LikeService;
import com.pard.weact.memberInformation.service.MemberInformationService;
import com.pard.weact.postPhoto.entity.PostPhoto;
import com.pard.weact.postPhoto.service.PostPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitPostService {

    private final HabitPostRepo habitPostRepo;
    private final PostPhotoService postPhotoService;
    private final UserService userService;
    private final LikeService likeService;
    private final MemberInformationService memberInformationService;
    private final UserRepo userRepo;

    @Transactional
    public Long createPost(UploadPhotoDto photo, CreateHabitPostDto request) {
        HabitPost post;
        User user = userRepo.findByUserId(request.getUserId()).orElseThrow();

        if (request.getIsHaemyeong()) {
            // 해명일 경우 사진 없이 저장
            post = HabitPost.builder()
                    .userId(request.getUserId())
                    .message(request.getMessage())
                    .isHaemyeong(true)
                    .date(LocalDate.now())
                    .roomId(request.getRoomId())
                    .build();

            habitPostRepo.save(post);

        } else {
            // 사진이 있는 경우
            PostPhoto savedPhoto;
            try {
                // 사진 저장 시 영속 상태 PostPhoto 객체를 받아옴
                savedPhoto = postPhotoService.save(photo.getPhoto());
            } catch (IOException e) {
                throw new RuntimeException("사진 저장 중 오류가 발생했습니다.", e);
            }

            // 저장된 PostPhoto 엔티티를 HabitPost에 세팅
            post = HabitPost.builder()
                    .userId(request.getUserId())
                    .message(request.getMessage())
                    .isHaemyeong(false)
                    .date(LocalDate.now())
                    .roomId(request.getRoomId())
                    .photo(savedPhoto)
                    .build();

            habitPostRepo.save(post);

            // 일반 post 완료시 달성율 갱신
            memberInformationService.plusHabitCount(user.getId(), request.getRoomId());
        }

        return post.getId();
    }

    public List<PostResultListDto> readAllInRoom(Long roomId, LocalDate date) {
        List<HabitPost> posts = habitPostRepo.findAllByRoomIdAndDate(roomId, date);
        List<PostResultListDto> result = new ArrayList<>();
        for (HabitPost post : posts) {
            result.add(convertListIntoDto(post));
        }
        return result;
    }

    public PostResultOneDto readOneInRoom(String userId, Long postId) {
        HabitPost post = habitPostRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return convertOneIntoDto(post, userId);
    }

    private PostResultListDto convertListIntoDto(HabitPost post) {
        String userName = userService.getUserNameById(post.getUserId());

        String path = null;
        if (post.getPhoto() != null) {
            path = postPhotoService.getPhotoPathById(post.getPhoto().getId());
        }

        Long likeCount = likeService.countLikes(post.getId());

        return PostResultListDto.builder()
                .userName(userName)
                .imageUrl(path)
                .likeCount(likeCount)
                .build();
    }

    private PostResultOneDto convertOneIntoDto(HabitPost post, String viewingUserId) {
        String userName = userService.getUserNameById(post.getUserId());

        String path = null;
        if (post.getPhoto() != null) {
            path = postPhotoService.getPhotoPathById(post.getPhoto().getId());
        }

        Long likeCount = likeService.countLikes(post.getId());

        Boolean liked = null;
        if (viewingUserId != null) {
            liked = likeService.isLiked(viewingUserId, post.getId());
        }

        return PostResultOneDto.builder()
                .userName(userName)
                .message(post.getMessage())
                .imageUrl(path)
                .likeCount(likeCount)
                .liked(liked)
                .build();
    }
}
