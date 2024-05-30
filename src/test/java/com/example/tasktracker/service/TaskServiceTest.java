package com.example.tasktracker.service;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.Task;
import com.example.tasktracker.models.User;
import com.example.tasktracker.repository.CourseRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.requests.CreateTaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private Course course;
    private User user;
    private CreateTaskRequest createTaskRequest;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);

        user = new User();
        user.setId(1L);

        createTaskRequest = new CreateTaskRequest(
                "Test Task",
                "This is a test task",
                "Work",
                LocalDateTime.now().plusDays(1),
                "Active",
                1L,
                1L
        );
    }

    @Test
    void testCreateTask() {
        when(courseRepository.findById(any())).thenReturn(Optional.of(course));
        when(userService.findById(any())).thenReturn(user);
        when(taskRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Task createdTask = taskService.createTask(course, createTaskRequest);

        assertNotNull(createdTask);
        assertEquals(createTaskRequest.title(), createdTask.getTitle());
        assertEquals(createTaskRequest.description(), createdTask.getDescription());
        assertEquals(createTaskRequest.category(), createdTask.getCategory());
        assertEquals(createTaskRequest.status(), createdTask.getStatus());
        assertEquals(course, createdTask.getCourse());
        assertEquals(user, createdTask.getUser());
    }

    @Test
    void testFindAllTasksByCourse() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.add(new Task());

        when(taskRepository.findByCourse(any())).thenReturn(tasks);

        List<Task> foundTasks = taskService.findAllTasksByCourse(course);

        assertNotNull(foundTasks);
        assertEquals(2, foundTasks.size());
    }

    @Test
    void testDeleteTask() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(any())).thenReturn(Optional.of(task));

        boolean deleted = taskService.deleteTask(1L);

        assertTrue(deleted);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testDeleteTasksByCourse() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.add(new Task());

        when(taskRepository.findByCourse(any())).thenReturn(tasks);

        taskService.deleteTasksByCourse(course);

        verify(taskRepository, times(1)).deleteAll(tasks);
    }

    @Test
    void testFindAllTasksByUserId() {
        User user = new User();
        user.setId(1L);

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.add(new Task());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(taskRepository.findByUser(any())).thenReturn(tasks);

        List<Task> foundTasks = taskService.findAllTasksByUserId(1L);

        assertNotNull(foundTasks);
        assertEquals(2, foundTasks.size());
    }
}