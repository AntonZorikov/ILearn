package com.example.ilearn.servicies;

import com.example.ilearn.entities.CourseEntity;
import com.example.ilearn.entities.UserEntity;
import com.example.ilearn.exceptions.CourseAlreadyExist;
import com.example.ilearn.models.CreateCourseInputs;
import com.example.ilearn.repositories.CourseRepository;
import com.example.ilearn.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    public List<CourseEntity> findAllCoursesByUserId(Long user_id){
        return courseRepository.findAllByAuthor_Id(user_id);
    }

    public boolean userIsAuthorOfCourse(Long userId, Long courseId) {
        Optional<CourseEntity> course = courseRepository.findById(courseId);
        if(course.isPresent() && Objects.equals(course.get().getUser_id(), userId)){
            return true;
        }
        else{
            return false;
        }
    }

    public CourseEntity getCourseById(Long courseId){
        Optional<CourseEntity> course = courseRepository.findById(courseId);
        return course.orElse(null);
    }
}
