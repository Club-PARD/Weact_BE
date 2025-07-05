package com.pard.weact.User.controller;

import com.pard.weact.User.dto.req.CreateUserDto;
import com.pard.weact.User.dto.res.AddUserDto;
import com.pard.weact.User.dto.res.AfterCreateUserDto;
import com.pard.weact.User.dto.res.ReadAllUserDto;
import com.pard.weact.User.dto.res.SearchUserDto;
import com.pard.weact.User.service.UserService;
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
    public ResponseEntity<AfterCreateUserDto> createUser(@RequestBody CreateUserDto req) {
        AfterCreateUserDto afterCreateUserDto = userService.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(afterCreateUserDto);
    }

    // 모든 리스트 보기
    @GetMapping("/")
    public ResponseEntity<List<ReadAllUserDto>> readAll() {
        List<ReadAllUserDto> users = userService.readAll();
        return ResponseEntity.ok(users);
    }

    // userId로 검색
    @GetMapping("/search/{userId}")
    public List<SearchUserDto> searchUser(@PathVariable String userId){
        return userService.searchUser(userId);
    }

    // user 초대 목록에 추가
    @GetMapping("/search/add/{userId}")
    public AddUserDto addUser(@PathVariable String userId){
        return userService.addUser(userId);
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

