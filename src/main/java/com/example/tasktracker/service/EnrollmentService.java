package com.example.tasktracker.service;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.Enrollment;
import com.example.tasktracker.models.EnrollmentId;
import com.example.tasktracker.models.User;
import com.example.tasktracker.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Enrollment enrollStudent(Course course, User student) {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setId(new EnrollmentId(course.getId(), student.getId()));
        enrollment.setCourse(course);
        enrollment.setUser(student);
        return enrollmentRepository.save(enrollment);
    }

}
