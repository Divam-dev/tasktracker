package com.example.tasktracker.repository;

import com.example.tasktracker.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // Additional query methods if needed
}
