package com.pard.weact.habitPost.service;

import com.pard.weact.habitPost.dto.req.CreateHabitPostDto;
import com.pard.weact.habitPost.dto.req.UploadPhotoDto;
import com.pard.weact.habitPost.entity.HabitPost;
import com.pard.weact.habitPost.repo.HabitPostRepo;
import com.pard.weact.postPhoto.service.PostPhotoService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HabitPostService {

    private final HabitPostRepo habitPostRepo;
    private final PostPhotoService postPhotoService;

    public void createPost(UploadPhotoDto photo, CreateHabitPostDto request) { // 정상 포스트

        try {
            // 1. 해명글이 아닐 경우에만 사진 저장
            if (!request.getIsHaemyeong()) {
                Long photoId = postPhotoService.saveImage(photo.getPhoto()); // IOException 발생 가능
                request.setPhotoId(photoId);
            }

            // 2. 인증글 저장 (사진 포함 or 해명)
            HabitPost post = HabitPost.builder()
                    .memberId(request.getMemberId())
                    .roomId(request.getRoomId())
                    .photoId(request.getPhotoId()) // 해명글이면 null 가능
                    .message(request.getMessage())
                    .isHaemyeong(request.getIsHaemyeong())
                    .date(LocalDate.now())
                    .build();

            habitPostRepo.save(post);

        } catch (IOException e) {
            // 예외 로그 출력
            throw new RuntimeException("사진 저장에 실패했습니다.");
        }
    }

}
