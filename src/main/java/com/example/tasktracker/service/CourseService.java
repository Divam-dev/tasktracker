package com.example.tasktracker.service;

import com.example.tasktracker.models.Course;
import com.example.tasktracker.models.User;
import com.example.tasktracker.repository.CourseRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.requests.CreateCourseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    public Course createCourse(CreateCourseRequest request) {
        if (request.courseName() == null || request.courseDescription().isEmpty()) {
            throw new IllegalArgumentException("Course name must not be empty");
        }

        Course course = new Course();
        course.setCourseName(request.courseName());
        course.setCourseDescription(request.courseDescription());

        User teacher = userRepository.findById(request.teacherId()).get();
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }


    public boolean deleteCourse(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            taskService.deleteTasksByCourse(course);
            courseRepository.delete(course);
            return true;
        }
        return false;
    }
}

