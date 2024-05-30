package com.example.tasktracker.repository;

import com.example.tasktracker.models.Enrollment;
import com.example.tasktracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUser(User user);
}
