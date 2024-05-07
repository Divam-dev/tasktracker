package com.example.tasktracker.requests;

import java.time.LocalDateTime;

public record JoinCourseRequest(Long user_id, Long course_id, LocalDateTime enrollmentDate) {}
