package com.example.ilearn.servicies;

import com.example.ilearn.entities.CartEntity;
import com.example.ilearn.entities.CourseEntity;
import com.example.ilearn.entities.UserEntity;
import com.example.ilearn.repositories.CartRepository;
import com.example.ilearn.repositories.CourseRepository;
import com.example.ilearn.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CartRepository cartRepository;

    public boolean buyCourse(Long userId, Long courseId){
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        Optional<CourseEntity> optionalCourse = courseRepository.findById(courseId);
        if(optionalUser.isPresent() && optionalCourse.isPresent()){
            UserEntity user = optionalUser.get();
            CourseEntity course = optionalCourse.get();
            CartEntity cart = new CartEntity(userId, courseId);
            cartRepository.save(cart);
            return true;
        }
        else {
            return false;
        }
    }
}
