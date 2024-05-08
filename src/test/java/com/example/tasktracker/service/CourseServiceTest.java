package com.example.tasktracker.service;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.User;
import com.example.tasktracker.repository.CourseRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.requests.CreateCourseRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private CourseService courseService;

    @Test
    void createCourse_WithValidRequest_ShouldCreateCourse() {
        // Arrange
        CreateCourseRequest request = new CreateCourseRequest("Course Name", "Course Description", 1L);
        User teacher = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Course course = courseService.createCourse(request);

        // Assert
        assertNotNull(course);
        assertEquals("Course Name", course.getCourseName());
        assertEquals("Course Description", course.getCourseDescription());
        assertEquals(teacher, course.getTeacher());
    }

    @Test
    void createCourse_WithInvalidRequest_ShouldThrowException() {
        // Arrange
        CreateCourseRequest request = new CreateCourseRequest(null, "", 1L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> courseService.createCourse(request));
    }

    @Test
    void getCourseById_WithExistingId_ShouldReturnCourse() {
        // Arrange
        Long courseId = 1L;
        Course course = new Course();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        Optional<Course> result = courseService.getCourseById(courseId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(course, result.get());
    }

    @Test
    void getCourseById_WithNonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act
        Optional<Course> result = courseService.getCourseById(courseId);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void deleteCourse_WithExistingCourse_ShouldDeleteCourseAndTasks() {
        // Arrange
        Long courseId = 1L;
        Course course = new Course();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        boolean result = courseService.deleteCourse(courseId);

        // Assert
        assertTrue(result);
        verify(taskService, times(1)).deleteTasksByCourse(course);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void deleteCourse_WithNonExistingCourse_ShouldReturnFalse() {
        // Arrange
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act
        boolean result = courseService.deleteCourse(courseId);

        // Assert
        assertFalse(result);
        verifyNoInteractions(taskService);
    }
}