package com.example.ilearn.controllers;

import com.example.ilearn.exceptions.CourseAlreadyExist;
import com.example.ilearn.exceptions.UserNotFound;
import com.example.ilearn.models.CreateCourseInputs;
import com.example.ilearn.servicies.AuthorizationService;
import com.example.ilearn.servicies.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CourseController {
    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CourseService courseService;

    @PostMapping("/create_course")
    private String createCourse(CreateCourseInputs createCourseInputs, Model model, HttpSession session) throws Exception {
        try {
            if (authorizationService.isSessionActive(session)) {
                courseService.createCourse(createCourseInputs, session);
                return "home";
            } else {
                model.addAttribute("error", true);
                model.addAttribute("errorMessage", "Not enough rights");
                return "error";
            }
        } catch (CourseAlreadyExist | UserNotFound e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "create_course";
        }
    }
}
