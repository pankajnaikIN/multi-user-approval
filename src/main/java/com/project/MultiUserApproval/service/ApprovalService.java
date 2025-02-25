package com.project.MultiUserApproval.service;

import com.project.MultiUserApproval.TaskStatus;
import com.project.MultiUserApproval.entity.Approval;
import com.project.MultiUserApproval.entity.Task;
import com.project.MultiUserApproval.entity.User;
import com.project.MultiUserApproval.exception.ResourceNotFoundException;
import com.project.MultiUserApproval.exception.UnauthorizedException;
import com.project.MultiUserApproval.repository.ApprovalRepository;
import com.project.MultiUserApproval.repository.TaskRepository;
import com.project.MultiUserApproval.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public String approveTask(Long taskId, Long userId, String comment) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!task.getApprovers().contains(user)) {
            throw new RuntimeException("User is not an assigned approver.");
        }

        Approval approval = new Approval();
        approval.setTask(task);
        approval.setApprover(user);
        approval.setComment(comment);
        approval.setApproved(true);

        approvalRepository.save(approval);

        // Notify task creator when an approver signs off
        String subject = "Task Approved";
        String message = "<p>User <strong>" + user.getName() + "</strong> has approved your task '<strong>" + task.getTitle() + "</strong>'.</p>";

        emailService.sendEmail(task.getApprovers().get(0).getEmail(), subject, message);

        // Check if 3 approvals are received
        long approvalCount = task.getApprovals().stream().filter(Approval::isApproved).count();
        if (approvalCount >= 3) {
            task.setStatus(TaskStatus.APPROVED);
            taskRepository.save(task);

            // Notify all users when task is fully approved
            String finalSubject = "Task Fully Approved!";
            String finalMessage = "<p>The task '<strong>" + task.getTitle() + "</strong>' has been approved by all assigned users.</p>";

            task.getApprovers().forEach(approver ->
                    emailService.sendEmail(approver.getEmail(), finalSubject, finalMessage)
            );
        }

        return "Task approved successfully.";
    }
}