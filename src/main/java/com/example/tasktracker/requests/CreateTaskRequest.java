package com.example.tasktracker.requests;

import java.time.LocalDateTime;

public record CreateTaskRequest(String title, String description, String category, LocalDateTime deadline, String status, Long course_id, Long user_id) {}