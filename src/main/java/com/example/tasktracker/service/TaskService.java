package com.example.tasktracker.service;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.Task;
import com.example.tasktracker.models.User;
import com.example.tasktracker.repository.CourseRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.requests.CreateTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    public Task createTask(Course course, CreateTaskRequest request) {
        Task task = new Task();
        task.setCourse(course);
        //category, deadline, description, due_date, status, title, course_id, user_id
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCategory(request.category());
        task.setStatus(request.status());
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

    public boolean deleteTask(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            taskRepository.delete(taskOptional.get());
            return true;
        }
        return false;
    }

    public void deleteTasksByCourse(Course course) {
        List<Task> tasks = taskRepository.findByCourse(course);
        taskRepository.deleteAll(tasks);
    }
    //getTasksByUser
    public List<Task> findAllTasksByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return taskRepository.findByUser(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
