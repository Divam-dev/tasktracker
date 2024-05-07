package com.example.tasktracker.service;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.Task;
import com.example.tasktracker.models.User;
import com.example.tasktracker.repository.CourseRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.requests.CreateTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserService userService;


    public Task createTask(Course course, CreateTaskRequest request) {
        Task task = new Task();
        task.setCourse(course);
        //category, deadline, description, due_date, status, title, course_id, user_id
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCategory(request.category());
        task.setStatus(request.status());
        task.setDueDate(request.due_date());
        task.setDeadline(LocalDate.from(request.deadline()));

        Course courses = courseRepository.findById(request.course_id()).get();
        task.setCourse(courses);

        User user = userService.findById(request.user_id());
        task.setUser(user);

        return taskRepository.save(task);

    }

    public List<Task> findAllTasksByCourse(Course course) {
        return taskRepository.findByCourse(course);
    }

    // Additional methods for task handling if needed
}
