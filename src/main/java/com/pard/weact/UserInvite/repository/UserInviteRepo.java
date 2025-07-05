package com.pard.weact.UserInvite.repository;

import com.pard.weact.User.entity.User;
import com.pard.weact.UserInvite.entity.UserInvite;
import com.pard.weact.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInviteRepo extends JpaRepository<UserInvite, Long> {
    @Query("SELECT ui FROM UserInvite ui WHERE ui.user.id = :userId AND ui.room.id = :roomId")
    UserInvite findByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);
}
