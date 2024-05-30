package com.example.tasktracker.controller.API;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.Enrollment;
import com.example.tasktracker.models.User;
import com.example.tasktracker.requests.CreateCourseRequest;
import com.example.tasktracker.requests.JoinCourseRequest;
import com.example.tasktracker.service.CourseService;
import com.example.tasktracker.service.EnrollmentService;
import com.example.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseApiController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    // Create a new course (only for teachers)
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("/create")
    public ResponseEntity<Course> createCourse(@RequestBody CreateCourseRequest request) {
        Course newCourse = courseService.createCourse(request);
        return ResponseEntity.ok(newCourse);
    }

    // Allow students to join a course using course ID
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping("/join")
    public ResponseEntity<Enrollment> joinCourse(@RequestBody JoinCourseRequest request) {
        Optional<Course> courseOptional = courseService.getCourseById(request.course_id());
        if (courseOptional.isPresent()) {
            // Find the student user by ID using the UserService
            User student = userService.findById(request.user_id());
            if (student != null) {
                Enrollment enrollment = enrollmentService.enrollStudent(courseOptional.get(), student);
                return ResponseEntity.ok(enrollment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        boolean deleted = courseService.deleteCourse(courseId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Course>> getCoursesByUserId(@PathVariable Long userId) {
        List<Course> courses = courseService.getCoursesByUserId(userId);
        return ResponseEntity.ok(courses);
    }
}