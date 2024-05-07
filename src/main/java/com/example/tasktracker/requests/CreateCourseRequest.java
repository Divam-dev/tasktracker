package com.example.tasktracker.requests;

public record CreateCourseRequest(String courseName, String courseDescription, Long teacherId) {}
