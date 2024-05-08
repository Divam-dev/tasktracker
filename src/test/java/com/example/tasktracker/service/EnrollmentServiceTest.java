package com.example.tasktracker.service;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.Enrollment;
import com.example.tasktracker.models.EnrollmentId;
import com.example.tasktracker.models.User;
import com.example.tasktracker.repository.EnrollmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Captor
    private ArgumentCaptor<Enrollment> enrollmentCaptor;

    @Test
    void enrollStudent() {
        // Arrange
        Long courseId = 1L;
        Long studentId = 2L;
        Course course = new Course();
        course.setId(courseId);
        User student = new User();
        student.setId(studentId);

        // Act
        enrollmentService.enrollStudent(course, student);

        // Assert
        verify(enrollmentRepository).save(enrollmentCaptor.capture());
        Enrollment capturedEnrollment = enrollmentCaptor.getValue();
        assertEquals(course, capturedEnrollment.getCourse());
        assertEquals(student, capturedEnrollment.getUser());
        assertEquals(LocalDate.now(), capturedEnrollment.getEnrollmentDate());
    }
}