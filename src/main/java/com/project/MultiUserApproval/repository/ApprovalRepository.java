package com.project.MultiUserApproval.repository;

import com.project.MultiUserApproval.entity.Approval;
import com.project.MultiUserApproval.entity.Task;
import com.project.MultiUserApproval.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findByTask(Task task);
    Optional<Approval> findByTaskAndApprover(Task task, User approver);
}
