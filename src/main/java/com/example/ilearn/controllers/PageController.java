package com.example.ilearn.controllers;

import com.example.ilearn.entities.CourseEntity;
import com.example.ilearn.entities.LessonEntity;
import com.example.ilearn.servicies.AuthorizationService;
import com.example.ilearn.servicies.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PageController {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/auth")
    public String auth(){
        return "authorization";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session){
        model.addAttribute("error", false);
        model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
        return "login";
    }

    @GetMapping("/signin")
    public String signin(Model model, HttpSession session){
        model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
        return "signin";
    }

    @GetMapping("/create_course")
    public String createCourse(HttpSession session, Model model){
        if (authorizationService.isSessionActive(session)) {
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "create_course";
        } else {
            model.addAttribute("error", true);
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            model.addAttribute("errorMessage", "Not enough rights");
            return "error";
        }
    }

    @GetMapping("author_courses")
    public String author_courses(HttpSession session, Model model){
        if(authorizationService.isUserAuthorBySession(session)){
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            model.addAttribute("courses", authorizationService.getUserBySession(session).getCourses());
            return "author_courses";
        }
        else{
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            model.addAttribute("errorMessage", "Not enough rights");
            return "error";
        }
    }

    @GetMapping("/edit_course")
    public String edit_course(@RequestParam Long courseId, Model model, HttpSession session){
        if(authorizationService.isUserAuthorBySession(session) &&
                courseService.userIsAuthorOfCourse(authorizationService.getUserBySession(session).getId(), courseId)){
            CourseEntity course = courseService.getCourseById(courseId);
            model.addAttribute("course", course);
            if(course.getLessons().size() > 0){
                model.addAttribute("lessonsFind", true);
                List<LessonEntity> sortedLessons = course.getLessons()
                        .stream()
                        .sorted(Comparator.comparingLong(LessonEntity::getId))
                        .collect(Collectors.toList());
                model.addAttribute("lessons", sortedLessons);
            }
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "edit_course";
        }
        else{
            model.addAttribute("errorMessage", "You are not the author of this course");
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "error";
        }
    }

    @GetMapping("/edit_lesson")
    public String edit_lesson(@RequestParam Long lessonId, Model model, HttpSession session){
        LessonEntity lesson = courseService.getLessonById(lessonId);
        if(lesson == null){
            model.addAttribute("errorMessage", "Lesson not found");
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "error";
        }
        if(authorizationService.isUserAuthorBySession(session) &&
                courseService.userIsAuthorOfCourse(authorizationService.getUserBySession(session).getId(), lesson.getCourse_id())){
            model.addAttribute("lesson", lesson);
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "edit_lesson";
        }
        else{
            model.addAttribute("errorMessage", "You are not the author of this Lesson");
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "error";
        }
    }
}
