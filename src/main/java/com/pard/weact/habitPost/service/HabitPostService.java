package com.pard.weact.habitPost.service;

import com.pard.weact.User.service.UserService;
import com.pard.weact.habitPost.dto.req.CreateHabitPostDto;
import com.pard.weact.habitPost.dto.res.PostResultDto;
import com.pard.weact.habitPost.dto.req.UploadPhotoDto;
import com.pard.weact.habitPost.entity.HabitPost;
import com.pard.weact.habitPost.repo.HabitPostRepo;
import com.pard.weact.postPhoto.service.PostPhotoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


    public void createPost(UploadPhotoDto photo, CreateHabitPostDto request) { // 인증을 올리면 일단 저장하고 상태만 바꾸기
        try {
            if (request.getIsHaemyeong()) { // 해명 글 -> 사진 필요 x, 다른 사람 인증 필요x
                HabitPost post = HabitPost.builder()
                        .userId(request.getUserId())
                        .message(request.getMessage())
                        .isHaemyeong(true)
                        .date(LocalDate.now())
                        .roomId(request.getRoomId())
                        .build();

                habitPostRepo.save(post);
            } else { // 일반적인 인증 글 -> 사진 필요 o, 다른 사람 인증 필요 o
                Long photoId = postPhotoService.saveImage(photo.getPhoto()); // IOException 발생 가능

                HabitPost post = HabitPost.builder()
                        .userId(request.getUserId())
                        .message(request.getMessage())
                        .isHaemyeong(true)
                        .date(LocalDate.now())
                        .roomId(request.getRoomId())
                        .photoId(photoId) // 사진 추가
                        .build();

                habitPostRepo.save(post);
            }
        } catch (IOException e) {
            // 예외 로그 출력
            throw new RuntimeException("사진 저장에 실패했습니다.");
        }
    }

    public List<PostResultDto> readAllInRoom(Long roomId, LocalDate date) {
        List<HabitPost> posts = habitPostRepo.findAllByRoomIdAndDate(roomId,date);

        List<PostResultDto> result = new ArrayList<>();
        for (HabitPost post : posts) {
            PostResultDto dto = convertIntoDto(post);
            result.add(dto);
        }

        return result;
    }

    public PostResultDto readOneInRoom(String userId, Long roomId, LocalDate date) {
        HabitPost post = habitPostRepo.findByUserIdAndRoomIdAndDate(userId, roomId, date)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 게시글이 존재하지 않습니다."));

        return convertIntoDto(post);
    }

    private PostResultDto convertIntoDto(HabitPost post) { // dto 가공 함수

        String userName = userService.getUserNameById(post.getUserId());

        String path = null;
        if (post.getPhotoId() != null) {
            path = postPhotoService.getPhotoPathById(post.getPhotoId());
        }

        return PostResultDto.builder()
                .userName(userName)
                .message(post.getMessage())
                .imageUrl(path)
                .build();
    }

}
