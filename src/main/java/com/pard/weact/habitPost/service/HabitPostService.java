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
import com.pard.weact.liked.service.LikedService;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.memberInformation.repository.MemberInformationRepo;
import com.pard.weact.postPhoto.entity.PostPhoto;
import com.pard.weact.postPhoto.service.PostPhotoService;
import com.pard.weact.room.entity.Room;
import com.pard.weact.room.repository.RoomRepo;
import com.pard.weact.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final CommentRepo commentRepo;
    private final MemberInformationRepo memberRepo;
    private final RoomService roomService;

    public Long createHabitPost(CreateHabitPostDto dto, MultipartFile image) throws IOException {
        PostPhoto photo = postPhotoService.uploadAndSave(image);
        System.out.println("photo saved: " + (photo != null ? photo.getId() : "null"));

        if (dto.getUserId() == null || dto.getRoomId() == null) {
            throw new IllegalArgumentException("UserId 또는 RoomId가 null입니다.");
        }

        User user = userRepo.findById(dto.getUserId()).orElseThrow();
        MemberInformation member = memberRepo.findByUserIdAndRoomId(dto.getUserId(), dto.getRoomId());
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
            member.plusHabitCount();
        }
        member.updateDoNothing();
        roomService.checkOneDayCount(room.getId());

        return habitPostRepo.save(post).getId();
    } // 해명하고 나뉘는 건 부차적인 문제

    public Long createHabitPostWithoutPhoto(CreateHabitPostDto dto) {
        if (dto.getUserId() == null || dto.getRoomId() == null) {
            throw new IllegalArgumentException("UserId 또는 RoomId가 null입니다.");
        }

        User user = userRepo.findById(dto.getUserId()).orElseThrow();
        MemberInformation member = memberRepo.findByUserIdAndRoomId(dto.getUserId(), dto.getRoomId());

        PostPhoto defaultPhoto = postPhotoService.getDefaultHaemyeongPhoto();

        HabitPost post = HabitPost.builder()
                .message(dto.getMessage())
                .photo(defaultPhoto) // 이미지 없이 저장
                .room(roomRepo.findById(dto.getRoomId()).orElseThrow())
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
            MemberInformation viewingMember = memberRepo.findByUserIdAndRoomId(
                    viewingUserId, post.getRoom().getId()
            );

            liked = likedService.isLiked(post.getId(), viewingMember.getId());
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
