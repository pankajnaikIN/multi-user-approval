package com.project.MultiUserApproval.controller;

import com.project.MultiUserApproval.entity.Approval;
import com.project.MultiUserApproval.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @PostMapping("/approve")
    public ResponseEntity<String> approveTask(@RequestBody Map<String, Object> payload) {
        Long taskId = Long.valueOf((Integer) payload.get("taskId"));
        Long userId = Long.valueOf((Integer) payload.get("userId"));
        String comment = (String) payload.get("comment");

        return ResponseEntity.ok(approvalService.approveTask(taskId, userId, comment));
    }
}