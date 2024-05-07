package com.example.tasktracker.controller.API;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.Enrollment;
import com.example.tasktracker.models.Task;
import com.example.tasktracker.models.User;
import com.example.tasktracker.requests.CreateCourseRequest;
import com.example.tasktracker.requests.CreateTaskRequest;
import com.example.tasktracker.requests.JoinCourseRequest;
import com.example.tasktracker.service.CourseService;
import com.example.tasktracker.service.EnrollmentService;
import com.example.tasktracker.service.TaskService;
import com.example.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseApiController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TaskService taskService;

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

    // Create a new task in a course (only for teachers)
//    @PreAuthorize("hasRole('ROLE_TEACHER')")
//    @PostMapping("/{courseId}/tasks")
//    public ResponseEntity<Task> createTask(@PathVariable Long courseId, @RequestBody Task task) {
//        Optional<Course> courseOptional = courseService.getCourseById(courseId);
//        if (courseOptional.isPresent()) {
//            Course course = courseOptional.get();
//            Task newTask = taskService.createTask(course, task);
//            return ResponseEntity.ok(newTask);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // Allow students to join a course using course ID
//    @PreAuthorize("hasRole('ROLE_STUDENT')")
//    @PostMapping("/{courseId}/join")
//    public ResponseEntity<Enrollment> joinCourse(@PathVariable Long courseId, @RequestBody User student) {
//        Optional<Course> courseOptional = courseService.getCourseById(courseId);
//        if (courseOptional.isPresent()) {
//            Course course = courseOptional.get();
//            Enrollment enrollment = enrollmentService.enrollStudent(course, student);
//            return ResponseEntity.ok(enrollment);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // Create a new task in a course (only for teachers)
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("/tasks/create")
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        Optional<Course> courseOptional = courseService.getCourseById(request.course_id());
        if (courseOptional.isPresent()) {
            Task newTask = taskService.createTask(courseOptional.get(), request);
            return ResponseEntity.ok(newTask);
        } else {
            return ResponseEntity.notFound().build();
        }
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

    // Get all tasks for a specific course
//    @GetMapping("/{courseId}/tasks")
//    public ResponseEntity<List<Task>> viewTasks(@RequestBody ViewTasksRequest request) {
//        Optional<Course> courseOptional = courseService.getCourseById(request.getCourseId());
//        if (courseOptional.isPresent()) {
//            List<Task> tasks = taskService.findAllTasksByCourse(courseOptional.get());
//            return ResponseEntity.ok(tasks);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

}