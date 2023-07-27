package com.example.ilearn.repositories;

import com.example.ilearn.entities.CourseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<CourseEntity, Long> {
    CourseEntity findByTitle(String title);
    List<CourseEntity> findAllByAuthor_Id(Long userId);
}
