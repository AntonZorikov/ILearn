package com.example.ilearn.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userid;

    private Long courseid;

    public CartEntity(Long userId, Long courseId){
        this.userid = userId;
        this.courseid = courseId;
    }
}
