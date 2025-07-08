package com.pard.weact.room.controller;

import com.pard.weact.room.dto.req.CreateRoomDto;
import com.pard.weact.room.dto.res.AfterCreateRoomDto;
import com.pard.weact.room.dto.res.CheckPointDto;
import com.pard.weact.room.dto.res.FinalRankingDto;
import com.pard.weact.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("")
    public AfterCreateRoomDto createRoom(@RequestBody CreateRoomDto createRoomDto){
        return roomService.createRoom(createRoomDto);
    }

    @GetMapping("/checkPoint/{roomId}")
    public CheckPointDto getCheckPoint(@PathVariable Long roomId){
        return roomService.getCheckPoint(roomId);
    }

    @GetMapping("/finalRanking{roomId}/{userId}")
    public FinalRankingDto getFinalRanking(@PathVariable Long roomId, @PathVariable Long userId){
        return roomService.getFinalRanking(roomId, userId);
    }

    @GetMapping("/oneDayCount")
    public boolean checkOneDayCount(Long roomId){
        return roomService.checkOneDayCount(roomId);
    }

    @GetMapping("/checkDays/{roomId}") // 오늘 인증하는 요일 맞는지
    public boolean checkDays(@PathVariable Long roomId){
        return roomService.checkDays(roomId);
    }

    @GetMapping("/checkThreeDay{roomId}")
    public boolean checkThreedays(@PathVariable Long roomId) {
        return roomService.checkThreeDays(roomId);
    }
}
