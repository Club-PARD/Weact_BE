package com.pard.weact.room.repository;

import com.pard.weact.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepo extends JpaRepository<Room, Long> {
}
