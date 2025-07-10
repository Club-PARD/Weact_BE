package com.pard.weact.habitPost.service;

import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.comment.dto.res.CommentDto;
import com.pard.weact.comment.entity.Comment;
import com.pard.weact.comment.repo.CommentRepo;
import com.pard.weact.habitPost.dto.req.CreateHabitPostDto;
import com.pard.weact.habitPost.dto.res.PostResultListDto;
import com.pard.weact.habitPost.dto.res.PostResultOneDto;
import com.pard.weact.habitPost.entity.HabitPost;
import com.pard.weact.habitPost.repo.HabitPostRepo;
import com.pard.weact.inAppNotification.entity.NotificationType;
import com.pard.weact.liked.service.LikedService;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.memberInformation.repository.MemberInformationRepo;
import com.pard.weact.memberInformation.service.MemberInformationService;
import com.pard.weact.postPhoto.entity.PostPhoto;
import com.pard.weact.postPhoto.service.PostPhotoService;
import com.pard.weact.room.entity.Room;
import com.pard.weact.room.repository.RoomRepo;
import com.pard.weact.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HabitPostService {

    private final HabitPostRepo habitPostRepo;
    private final RoomRepo roomRepo;
    private final UserRepo userRepo;
    private final PostPhotoService postPhotoService;
    private final LikedService likedService;
    private final MemberInformationRepo memberRepo;
    private final RoomService roomService;
    private final MemberInformationService memberInformationService;

    public Long createHabitPost(Long userId, CreateHabitPostDto dto, MultipartFile image) throws IOException {

        if (userId == null || dto.getRoomId() == null) {
            throw new IllegalArgumentException("UserId 또는 RoomId가 null입니다.");
        }

        boolean exists = habitPostRepo.existsByUser_IdAndDateAndRoom_Id(userId, LocalDate.now(), dto.getRoomId());


            if (exists) {
                System.out.println("🔥🔥🔥 인증 중복 발생");
                throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 오늘 인증을 완료했습니다.");
            }

        PostPhoto photo = postPhotoService.uploadAndSave(image);
        System.out.println("photo saved: " + (photo != null ? photo.getId() : "null"));

        MemberInformation member = memberRepo.findByUserIdAndRoomId(userId, dto.getRoomId());
        User user = userRepo.findById(userId).orElseThrow();
        Room room = roomRepo.findById(dto.getRoomId()).orElseThrow();


        HabitPost post = HabitPost.builder()
                .message(dto.getMessage())
                .photo(photo)
                .room(room)
                .user(user) // ← 추가
                .member(member)
                .date(LocalDate.now())
                .isHaemyeong(dto.getIsHaemyeong())
                .build();

        if(!post.isHaemyeong()){
            memberInformationService.plusHabitCount(user.getId(), room.getId());
        }
        member.updateDoNothing();
        roomService.checkOneDayCount(room.getId());
        roomService.notifyOthers(user, room, NotificationType.POST_INFORM);

        return habitPostRepo.save(post).getId();
    } // 해명하고 나뉘는 건 부차적인 문제

    public Long createHabitPostWithoutPhoto(Long userId, CreateHabitPostDto dto) {
        if (userId == null || dto.getRoomId() == null) {
            throw new IllegalArgumentException("UserId 또는 RoomId가 null입니다.");
        }


        boolean exists = habitPostRepo.existsByUser_IdAndDateAndRoom_Id(userId, LocalDate.now(), dto.getRoomId());


        if (exists) {
            System.out.println("🔥🔥🔥 인증 중복 발생");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 오늘 인증을 완료했습니다.");
        }

        MemberInformation member = memberRepo.findByUserIdAndRoomId(userId, dto.getRoomId());
        User user = userRepo.findById(userId).orElseThrow();
        Room room = roomRepo.findById(dto.getRoomId()).orElseThrow();


        PostPhoto defaultPhoto = postPhotoService.getDefaultHaemyeongPhoto();

        HabitPost post = HabitPost.builder()
                .message(dto.getMessage())
                .photo(defaultPhoto) // 해명 이미지 저장
                .room(room)
                .user(user)
                .member(member)
                .date(LocalDate.now())
                .isHaemyeong(dto.getIsHaemyeong())
                .build();

        return habitPostRepo.save(post).getId();
    }
    public List<PostResultListDto> readAllInRoom(Long roomId, LocalDate date) {
        List<HabitPost> posts = habitPostRepo.findAllByRoomIdAndDate(roomId, date);
        List<PostResultListDto> result = new ArrayList<>();
        for (HabitPost post : posts) {
            result.add(convertListIntoDto(post));
        }
        return result;
    }

    public PostResultOneDto readOneInRoom(Long userId, Long postId) {
        HabitPost post = habitPostRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return convertOneIntoDto(post, userId);
    }

    private PostResultListDto convertListIntoDto(HabitPost post) {
        String userName = post.getUser().getUserName();

        String path = null;
        if (post.getPhoto() != null) {
            path = postPhotoService.getPhotoPathById(post.getPhoto().getId());
        }

        Long likeCount = likedService.countLikes(post.getId());

        return PostResultListDto.builder()
                .userName(userName)
                .imageUrl(path)
                .likeCount(likeCount)
                .postId(post.getId())
                .build();
    }

    private PostResultOneDto convertOneIntoDto(HabitPost post, Long viewingUserId) {
        String userName = post.getUser().getUserName();

        String path = null;
        if (post.getPhoto() != null) {
            path = postPhotoService.getPhotoPathById(post.getPhoto().getId());
        }

        Long likeCount = likedService.countLikes(post.getId());

        Boolean liked = null;
        if (viewingUserId != null) {
            liked = likedService.isLiked(post.getId(), viewingUserId);
        }

        List<CommentDto> commentDtos = post.getComments().stream()
                .map(c -> new CommentDto(
                        c.getContent(),
                        c.getUser().getUserName(),
                        c.getCreatedAt()
                ))
                .toList();

        return PostResultOneDto.builder()
                .userName(userName)
                .message(post.getMessage())
                .imageUrl(path)
                .likeCount(likeCount)
                .liked(liked)
                .comments(commentDtos)
                .build();
    }



}
