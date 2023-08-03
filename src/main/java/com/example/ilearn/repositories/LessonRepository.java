package com.example.ilearn.repositories;

import com.example.ilearn.entities.LessonEntity;
import org.springframework.data.repository.CrudRepository;

public interface LessonRepository extends CrudRepository<LessonEntity, Long> {
}
