package com.pard.weact.comment.repo;

import com.pard.weact.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByHabitPostIdOrderByCreatedAtAsc(Long habitPostId);
}

