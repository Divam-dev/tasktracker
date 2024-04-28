package com.example.tasktracker.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "course_student")
public class Enrollment implements Serializable {

    @EmbeddedId
    private EnrollmentId id;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User student;

    // Constructors, Getters and Setters
}