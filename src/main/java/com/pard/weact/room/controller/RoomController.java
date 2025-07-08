package com.pard.weact.room.controller;

import com.pard.weact.User.entity.User;
import com.pard.weact.room.dto.req.CreateRoomDto;
import com.pard.weact.room.dto.res.AfterCreateRoomDto;
import com.pard.weact.room.dto.res.CheckPointDto;
import com.pard.weact.room.dto.res.FinalRankingDto;
import com.pard.weact.room.service.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "jwtAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("")
    public AfterCreateRoomDto createRoom(@AuthenticationPrincipal User user, @RequestBody CreateRoomDto createRoomDto){
        return roomService.createRoom(user.getId(), createRoomDto);
    }

    @GetMapping("/checkPoint/{roomId}")
    public CheckPointDto getCheckPoint(@PathVariable Long roomId){
        return roomService.getCheckPoint(roomId);
    }

    @GetMapping("/finalRanking/{roomId}")
    public FinalRankingDto getFinalRanking(@PathVariable Long roomId, @AuthenticationPrincipal User user){
        return roomService.getFinalRanking(roomId, user.getId());
    }

    @GetMapping("/oneDayCount")
    public boolean checkOneDayCount(Long roomId){
        return roomService.checkOneDayCount(roomId);
    }

    @GetMapping("/checkDays/{roomId}") // 오늘 인증하는 요일 맞는지
    public boolean checkDays(@PathVariable Long roomId){
        return roomService.checkDays(roomId);
    }

    @GetMapping("/checkThreeDay/{roomId}")
    public boolean checkThreedays(@PathVariable Long roomId) {
        return roomService.checkThreeDays(roomId);
    }
}
