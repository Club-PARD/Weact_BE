package com.pard.weact.User.controller;

import com.pard.weact.User.dto.req.CreateUserDto;
import com.pard.weact.User.dto.req.LoginDto;
import com.pard.weact.User.dto.res.*;
import com.pard.weact.User.entity.User;
import com.pard.weact.User.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.pard.weact.login.security.JwtUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    // 회원가입
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody CreateUserDto req) {
        userService.createUser(req); // 더 이상 AfterCreateUserDto 안 써도 됨
        String token = jwtUtil.generateToken(req.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }

    @GetMapping("/checkDuplicated/{userId}")
    public boolean checkDuplicated(@PathVariable String userId) {
        return userService.checkDuplicated(userId);
    }

    // 모든 리스트 보기
    @GetMapping("/")
    public ResponseEntity<List<ReadAllUserDto>> readAll() {
        List<ReadAllUserDto> users = userService.readAll();
        return ResponseEntity.ok(users);
    }

    // 홈화면
    @GetMapping("/home")
    public HomeScreenDto getHomeScreen(@AuthenticationPrincipal User user) {
        return userService.getHomeScreen(user.getId());
    }

    // userId로 검색
    @GetMapping("/search/{userId}")
    public List<SearchUserDto> searchUser(@PathVariable String userId) {
        return userService.searchUser(userId);
    }

    // user 초대 목록에 추가
    @GetMapping("/search/add/{userId}")
    public AddUserDto addUser(@PathVariable String userId) {
        return userService.addUser(userId);
    }

    // 수정
    @PatchMapping("")
    public ResponseEntity<String> updateById(@AuthenticationPrincipal User user, @RequestBody CreateUserDto req) {
        userService.updateById(user.getId(), req);
        return ResponseEntity.ok("입력한 " + user.getId() + "번 내용 수정완료!");
    }

    // 삭제
    @DeleteMapping("")
    public ResponseEntity<String> deleteById(@AuthenticationPrincipal User user) {
        userService.deleteById(user.getId());
        return ResponseEntity.ok("입력한 " + user.getId() + "번 내용 삭제완료!");
    }

    // user profile 사진 수정
    @PostMapping(value = "/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadProfilePhoto(
            @AuthenticationPrincipal User user,
            @RequestPart("image") MultipartFile image
    ) {
        userService.uploadProfilePhoto(user.getId(), image);
        return ResponseEntity.ok().build();
    }

    // profile 사진 잘 들어갔는지 확인 -> 따로 구현 x
    @GetMapping("/profile")
    @Operation(summary = "현재 로그인한 유저의 프로필 정보 조회")
    public ResponseEntity<UserProfileDto> getMyProfile(@AuthenticationPrincipal User user) {
        UserProfileDto dto = userService.getMyProfile(user);
        return ResponseEntity.ok(dto);
    }

}

