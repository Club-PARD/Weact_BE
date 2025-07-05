package com.pard.weact.User.repo;

import com.pard.weact.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByUserId(String userId);

    List<User> findByUserIdContaining(String userId);
    Optional<User> findByUserId(String userId); // 이걸 추가!
}
