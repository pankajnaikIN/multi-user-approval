package com.project.MultiUserApproval.controller;

import com.project.MultiUserApproval.entity.Task;
import com.project.MultiUserApproval.exception.ResourceNotFoundException;
import com.project.MultiUserApproval.service.ApprovalService;
import com.project.MultiUserApproval.service.TaskService;
//import com.project.MultiUserApproval.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Map<String, Object> payload) {
        String title = (String) payload.get("title");
        String description = (String) payload.get("description");
        List<Integer> approverIds = (List<Integer>) payload.get("approvers");

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);

        return ResponseEntity.ok(taskService.createTask(task, approverIds.stream().map(Long::valueOf).toList()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok((Task) taskService.getTaskById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId)));
    }
}