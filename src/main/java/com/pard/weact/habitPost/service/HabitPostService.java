package com.pard.weact.habitPost.service;

import com.pard.weact.User.service.UserService;
import com.pard.weact.habitPost.dto.req.CreateHabitPostDto;
import com.pard.weact.habitPost.dto.res.PostResultListDto;
import com.pard.weact.habitPost.dto.res.PostResultOneDto;
import com.pard.weact.habitPost.dto.req.UploadPhotoDto;
import com.pard.weact.habitPost.entity.HabitPost;
import com.pard.weact.habitPost.repo.HabitPostRepo;
import com.pard.weact.liked.service.LikeService;
import com.pard.weact.postPhoto.entity.PostPhoto;
import com.pard.weact.postPhoto.service.PostPhotoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Long createPost(UploadPhotoDto photo, CreateHabitPostDto request) {
        HabitPost post;

        if (request.getIsHaemyeong()) {
            post = HabitPost.builder()
                    .userId(request.getUserId())
                    .message(request.getMessage())
                    .isHaemyeong(true)
                    .date(LocalDate.now())
                    .roomId(request.getRoomId())
                    .build();
        } else {
            PostPhoto savedPhoto;
            try {
                Long photoId = postPhotoService.save(photo.getPhoto());
                savedPhoto = PostPhoto.builder()
                        .id(photoId)
                        .build();
            } catch (IOException e) {
                throw new RuntimeException("사진 저장 중 오류가 발생했습니다.", e);
            }

            post = HabitPost.builder()
                    .userId(request.getUserId())
                    .message(request.getMessage())
                    .isHaemyeong(false)
                    .date(LocalDate.now())
                    .roomId(request.getRoomId())
                    .photo(savedPhoto)
                    .build();
        }

        habitPostRepo.save(post);
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