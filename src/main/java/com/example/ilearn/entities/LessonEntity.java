package com.example.ilearn.entities;

import com.example.ilearn.models.CreateLessonInputs;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
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

    public LessonEntity(CreateLessonInputs lessonInputs){
        this.title = lessonInputs.getTitle();
        this.course_id = lessonInputs.getCourse_id();
    }
}
