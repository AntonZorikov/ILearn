package com.example.ilearn.repositories;

import com.example.ilearn.entities.CartEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends CrudRepository<CartEntity, Long> {
    CartEntity findByUseridAndCourseid(Long userId, Long courseId);
    List<CartEntity> findAllByUserid(Long userId);
}
