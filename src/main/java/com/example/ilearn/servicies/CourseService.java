package com.example.ilearn.servicies;

import com.example.ilearn.entities.CourseEntity;
import com.example.ilearn.entities.UserEntity;
import com.example.ilearn.exceptions.CourseAlreadyExist;
import com.example.ilearn.models.CreateCourseInputs;
import com.example.ilearn.repositories.CourseRepository;
import com.example.ilearn.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public CourseEntity createCourse(CreateCourseInputs createCourseInputs, HttpSession session) throws Exception {
        if(courseRepository.findByTitle(createCourseInputs.getTitle()) == null){
            Optional<UserEntity> author = userRepository.findById((Long) session.getAttribute("userId"));
            if(author.isPresent()) {
                CourseEntity courseEntity = new CourseEntity(createCourseInputs, author.get());
                author.get().getCourses().add(courseEntity);
                return courseRepository.save(courseEntity);
            }
            else{
                throw new Exception("User not found");
            }
        }
        else{
            throw new CourseAlreadyExist("Course with the same name already exists");
        }
    }
}
