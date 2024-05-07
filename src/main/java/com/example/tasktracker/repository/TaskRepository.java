package com.example.tasktracker.repository;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCourse(Course course);
}
