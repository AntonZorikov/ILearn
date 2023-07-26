package com.example.ilearn.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lessons")
@Getter
@Setter
public class LessonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @Column(columnDefinition = "varchar(50)")
    private String title;

    private String text;

    private int rating;

    private String video_id;

    private String file_id;

    @Column(name = "course_id", insertable = false, updatable = false)
    private Long course_id;

}
