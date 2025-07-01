package com.pard.weact.User.repo;

import com.pard.weact.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByUserId(String userId);
}
