package com.example.ilearn.servicies;

import com.example.ilearn.entities.CourseEntity;
import com.example.ilearn.entities.LessonEntity;
import com.example.ilearn.entities.UserEntity;
import com.example.ilearn.exceptions.CourseAlreadyExist;
import com.example.ilearn.exceptions.CourseNotExist;
import com.example.ilearn.exceptions.IncorrectData;
import com.example.ilearn.models.CreateCourseInputs;
import com.example.ilearn.models.CreateLessonInputs;
import com.example.ilearn.repositories.CourseRepository;
import com.example.ilearn.repositories.LessonRepository;
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

    @Autowired
    private LessonRepository lessonRepository;

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

    public LessonEntity createLesson(CreateLessonInputs lessonInputs) throws CourseNotExist, IncorrectData {
        if(lessonInputs.getTitle() == null || lessonInputs.getTitle().equals("")){
            throw new IncorrectData("Title should be longer");
        }
        LessonEntity lesson = new LessonEntity(lessonInputs);
        Optional<CourseEntity> course = courseRepository.findById(lesson.getCourse_id());
        if(course.isPresent()){
            lesson.setCourse(course.get());
        }
        else{
            throw new CourseNotExist("Course does not exist");
        }
        course.get().getLessons().add(lesson);
        return lessonRepository.save(lesson);
    }

    public void changeCourseTitle(String title, Long courseId){
        Optional<CourseEntity> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()) {
            CourseEntity course = optionalCourse.get();
            course.setTitle(title);
            courseRepository.save(course);
        }
    }

    public void changeCoursePrice(Long price, Long courseId){
        Optional<CourseEntity> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()) {
            CourseEntity course = optionalCourse.get();
            course.setPrice(price);
            courseRepository.save(course);
        }
    }

    public LessonEntity getLessonById(Long lessonId){
        Optional<LessonEntity> lesson = lessonRepository.findById(lessonId);
        return lesson.orElse(null);

    }

    public void changeLessonTitle(String title, Long lessonId){
        Optional<LessonEntity> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isPresent()) {
            LessonEntity lesson = optionalLesson.get();
            lesson.setTitle(title);
            lessonRepository.save(lesson);
        }
    }

    public void changeLessonText(String text, Long lessonId){
        Optional<LessonEntity> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isPresent()) {
            LessonEntity lesson = optionalLesson.get();
            lesson.setText(text);
            lessonRepository.save(lesson);
        }
    }

}
