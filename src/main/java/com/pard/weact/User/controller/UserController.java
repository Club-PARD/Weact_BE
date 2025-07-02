package com.pard.weact.User.controller;

import com.pard.weact.User.dto.req.CreateUserDto;
import com.pard.weact.User.dto.res.ReadAllUserDto;
import com.pard.weact.User.service.UserService;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/")
    public ResponseEntity<String> createUser(@RequestBody CreateUserDto req) {
        userService.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
    }

    // 모든 리스트 보기
    @GetMapping("/")
    public ResponseEntity<List<ReadAllUserDto>> readAll() {
        List<ReadAllUserDto> users = userService.readAll();
        return ResponseEntity.ok(users);
    }

    // 수정
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateById(@PathVariable Long id, @RequestBody CreateUserDto req) {
        userService.updateById(id, req);
        return ResponseEntity.ok("입력한 " + id + "번 내용 수정완료!");
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("입력한 " + id + "번 내용 삭제완료!");
    }
}

