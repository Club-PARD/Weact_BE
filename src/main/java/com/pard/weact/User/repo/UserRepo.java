package com.pard.weact.User.repo;

import com.pard.weact.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByUserId(String userId);

    List<User> findByUserIdContaining(String userId);
}
