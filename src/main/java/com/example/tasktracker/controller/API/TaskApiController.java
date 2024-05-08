package com.example.tasktracker.controller.API;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.Task;
import com.example.tasktracker.requests.CreateTaskRequest;
import com.example.tasktracker.service.CourseService;
import com.example.tasktracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class TaskApiController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TaskService taskService;

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

    // Get all tasks for a specific course
    @GetMapping("/{courseId}/tasks")
    public ResponseEntity<List<Task>> getTasksByCourse(@PathVariable Long courseId) {
        Optional<Course> courseOptional = courseService.getCourseById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            List<Task> tasks = taskService.findAllTasksByCourse(course);
            return ResponseEntity.ok(tasks);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        boolean deleted = taskService.deleteTask(taskId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //get all tasks from all courses where user is a student
    @GetMapping("/user/{userId}/tasks")
    public ResponseEntity<List<Task>> getAllTasksForUser(@PathVariable Long userId) {
        List<Task> tasks = taskService.findAllTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

}