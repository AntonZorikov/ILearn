package com.example.ilearn.controllers;

import com.example.ilearn.servicies.AuthorizationService;
import com.example.ilearn.servicies.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String login(){
        return "login";
    }

    @GetMapping("/signin")
    public String signin(){
        return "signin";
    }

    @GetMapping("/create_course")
    public String createCourse(HttpSession session, Model model){
        if (authorizationService.isSessionActive(session)) {
            return "create_course";
        } else {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "Not enough rights");
            return "error";
        }
    }

    @GetMapping("author_courses")
    public String author_courses(HttpSession session, Model model){
        if(authorizationService.isUserAuthorBySession(session)){
            model.addAttribute("courses", authorizationService.getUserBySession(session).getCourses());
            return "author_courses";
        }
        else{
            model.addAttribute("errorMessage", "Not enough rights");
            return "error";
        }
    }

    @GetMapping("/edit_course")
    public String edit_course(@RequestParam Long courseId, Model model, HttpSession session){
        if(authorizationService.isUserAuthorBySession(session) &&
                courseService.userIsAuthorOfCourse(authorizationService.getUserBySession(session).getId(), courseId)){
            model.addAttribute("course", courseService.getCourseById(courseId));
            return "edit_course";
        }
        else{
            model.addAttribute("errorMessage", "You are not the author of this course");
            return "error";
        }
    }
}
