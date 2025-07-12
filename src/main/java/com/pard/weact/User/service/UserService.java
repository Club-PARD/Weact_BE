package com.pard.weact.User.service;

import com.pard.weact.User.dto.req.CreateUserDto;
import com.pard.weact.User.dto.res.*;
import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.memberInformation.repository.MemberInformationRepo;
import com.pard.weact.postPhoto.dto.ImageFile;
import com.pard.weact.postPhoto.service.ImageUploader;
import com.pard.weact.room.entity.Room;
import com.pard.weact.room.repository.RoomRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;
    private final MemberInformationRepo memberInformationRepo;
    private final ImageUploader imageUploader;

    private final String defaultProfileImageUrl = "https://weact-habit-app-image.s3.ap-northeast-2.amazonaws.com/basic/2e7029b2-18e1-4c5d-a811-9a83c3d73a1f";

    public AfterCreateUserDto createUser(CreateUserDto req){

        // 아이디가 중복이라면 회원가입 못하게 막아둠.
        if(userRepo.existsByUserId(req.getUserId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 아이디입니다.");
        }

        User user = User.builder()
                .userName(req.getUserName())
                .gender(req.getGender())
                .userId(req.getUserId())
                .pw(req.getPw())
                .profilePhoto(defaultProfileImageUrl)
                .build();
        userRepo.save(user);

        return AfterCreateUserDto.builder()
                .userId(user.getUserId())
                .id(user.getId()).build();
    }

    public boolean checkDuplicated(String userId){
        return userRepo.existsByUserId(userId);
    }

    public HomeScreenDto getHomeScreen(Long userId){
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        List<Room> rooms = memberInformationRepo.findByUserId(userId)
                .stream()
                .map(MemberInformation::getRoom)
                .distinct()
                .toList();


        List<RoomInformationDto> roomInfos = rooms.stream()
                .map(room -> RoomInformationDto.builder()
                        .roomName(room.getRoomName())
                        .dayCountByWeek(room.getDayCountByWeek())
                        .period(room.getPeriodFormatted())
                        .roomId(room.getId())
                        .memberCount(room.getMemberCount())
                        .habit(memberInformationRepo.findByUserIdAndRoomId(userId, room.getId()).getHabit())
                        .percent(memberInformationRepo.findByUserIdAndRoomId(userId, room.getId()).getPercent())
                        .build())
                .toList();

        return HomeScreenDto.builder()
                .month(month)
                .day(day)
                .roomInformationDtos(roomInfos)
                .build();
    }

    public List<ReadAllUserDto> readAll(){
        List<User> users = userRepo.findAll();
        List<ReadAllUserDto> readAllUserDtos = users.stream().map(user ->
                ReadAllUserDto.builder()
                        .id(user.getId())
                        .userName(user.getUserName())
                        .gender(user.getGender())
                        .userId(user.getUserId())
                        .build()).toList();
        return readAllUserDtos;
    }

    public List<SearchUserDto> searchUser(String userId){
        List<User> users = userRepo.findByUserIdContaining(userId);

        return users.stream()
                .map( user -> SearchUserDto.builder()
                        .userId(user.getUserId())
                        .id(user.getId())
                        .build())
                .toList();
    }

    public AddUserDto addUser(String userId){
        User user = userRepo.findByUserId(userId).orElseThrow();

        return AddUserDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .imageUrl(user.getProfilePhotoOrDefault())
                .build();
    }

    @Transactional
    public void updateById(Long Id, CreateUserDto req){
        Optional<User> optionalUser = userRepo.findById(Id);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.update(req);
            userRepo.save(user);// 이게 없으면 db에 저장 안됨!!!
        }
        else{
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다. ID: " + Id);
        }
    }

    @Transactional
    public void deleteById(Long Id){
        Optional<User> optionalUser = userRepo.findById(Id);
        if(optionalUser.isPresent()) {
            userRepo.deleteById(Id);
        }
        else{
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다. ID: " + Id);
        }
    }
    @Transactional
    public void uploadProfilePhoto(Long userId, MultipartFile multipartFile) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        ImageFile imageFile = convertToImageFile(multipartFile);
        String imageUrl = imageUploader.uploadProfileImage(imageFile);

        user.updateProfilePhoto(imageUrl); // 엔티티에 추가 필요
    }

    private ImageFile convertToImageFile(MultipartFile multipartFile) {
        try {
            return new ImageFile(
                    UUID.randomUUID().toString(),  // hashedName
                    multipartFile.getContentType(),
                    multipartFile.getSize(),
                    multipartFile.getInputStream()
            );
        } catch (IOException e) {
            throw new RuntimeException("이미지를 읽는 데 실패했습니다.", e);
        }
    }

    public UserProfileDto getMyProfile(User user) {
        String imageUrl = user.getProfilePhoto() != null && !user.getProfilePhoto().isBlank()
                ? user.getProfilePhoto()
                : defaultProfileImageUrl;

        return UserProfileDto.builder()
                .userName(user.getUserName())
                .profilePhoto(imageUrl)
                .build();
    }
}
