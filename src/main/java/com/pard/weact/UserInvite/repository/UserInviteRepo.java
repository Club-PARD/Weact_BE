package com.pard.weact.UserInvite.repository;

import com.pard.weact.UserInvite.entity.UserInvite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInviteRepo extends JpaRepository<UserInvite, Long> {
}
