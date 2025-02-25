package com.project.MultiUserApproval.service;

import com.project.MultiUserApproval.entity.Approval;
import com.project.MultiUserApproval.entity.Task;
import com.project.MultiUserApproval.entity.User;
import com.project.MultiUserApproval.exception.ResourceNotFoundException;
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
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public Task createTask(Task task, List<Long> approverIds) {
        List<User> approvers = userRepository.findAllById(approverIds);
        if (approvers.size() != 3) {
            throw new IllegalArgumentException("A task must have exactly 3 approvers.");
        }
        task.setApprovers(approvers);
        Task savedTask = taskRepository.save(task);

        // Send email to approvers
        String subject = "New Task Assigned for Approval";
        String message = "A new task '" + task.getTitle() + "' has been assigned to you for approval.";

        approvers.forEach(approver ->
                emailService.sendEmail(approver.getEmail(), subject, message)
        );

        return savedTask;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Object> getTaskById(Long taskId) {

        return Optional.ofNullable(taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId)));
    }
}