package com.project.MultiUserApproval.repository;

import com.project.MultiUserApproval.entity.Comment;
import com.project.MultiUserApproval.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task);
}
