package com.pard.weact.room.controller;

import com.pard.weact.room.dto.req.CreateRoomDto;
import com.pard.weact.room.dto.res.AfterCreateRoomDto;
import com.pard.weact.room.dto.res.CheckPointDto;
import com.pard.weact.room.dto.res.FinalRankingDto;
import com.pard.weact.room.service.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/checkPoint/{roomId}")
    public CheckPointDto getCheckPoint(@PathVariable Long roomId){
        return roomService.getCheckPoint(roomId);
    }

    @GetMapping("/finalRanking{roomId}")
    public FinalRankingDto getFinalRanking(@PathVariable Long roomId){
        return roomService.getFinalRanking(roomId);
    }

    @GetMapping("/oneDayCount")
    public boolean checkOneDayCount(Long roomId){
        return roomService.checkOneDayCount(roomId);
    }

    @GetMapping("/checkDays/{roomId}")
    public boolean checkDays(@PathVariable Long roomId){
        return roomService.checkDays(roomId);
    }

    @PostMapping("")
    public AfterCreateRoomDto createRoom(@RequestBody CreateRoomDto createRoomDto){
        return roomService.createRoom(createRoomDto);
    }
}
