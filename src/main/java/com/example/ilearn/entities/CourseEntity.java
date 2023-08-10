package com.example.ilearn.entities;

import com.example.ilearn.models.CreateCourseInputs;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity author;

    @Column(columnDefinition = "varchar(50)")
    private String title;

    private String description;

    private Long price;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long user_id;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<LessonEntity> lessons;

    public CourseEntity(CreateCourseInputs createCourseInputs, UserEntity author) {
        this.title = createCourseInputs.getTitle();
        this.description = createCourseInputs.getDescription();
        this.price = createCourseInputs.getPrice();
        this.user_id = author.getId();
        this.author = author;
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

}
