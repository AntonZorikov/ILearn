package com.example.ilearn.controllers;

import com.example.ilearn.entities.CourseEntity;
import com.example.ilearn.exceptions.*;
import com.example.ilearn.models.CreateCourseInputs;
import com.example.ilearn.models.CreateLessonInputs;
import com.example.ilearn.servicies.AuthorizationService;
import com.example.ilearn.servicies.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/create_lesson")
    private String create_lesson(CreateLessonInputs lessonInputs, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (authorizationService.isUserAuthorBySession(session) &&
                courseService.userIsAuthorOfCourse(authorizationService.getUserBySession(session).getId(), lessonInputs.getCourse_id())) {
            try {
                courseService.createLesson(lessonInputs);
                redirectAttributes.addAttribute("courseId", lessonInputs.getCourse_id());
                return "redirect:/edit_course";
            } catch (CourseNotExist | IncorrectData exception) {
//                model.addAttribute("error", true);
//                model.addAttribute("errorMessage", exception.getMessage());
                redirectAttributes.addAttribute("courseId", lessonInputs.getCourse_id());
                return "redirect:/edit_course";
            }
        } else {
            model.addAttribute("errorMessage", "Not enough rights");
            return "error";
        }
    }

    @PostMapping("/change_course_title")
    private String change_course_title(@RequestParam String title, @RequestParam Long course_id, HttpSession session,
                                       Model model, RedirectAttributes redirectAttributes) {
        if (authorizationService.isUserAuthorBySession(session) &&
                courseService.userIsAuthorOfCourse(authorizationService.getUserBySession(session).getId(), course_id)) {
            courseService.changeCourseTitle(title, course_id);
            redirectAttributes.addAttribute("courseId", course_id);
            return "redirect:/edit_course";
        } else {
            model.addAttribute("errorMessage", "Not enough rights");
            return "error";
        }
    }

    @PostMapping("/change_course_price")
    private String change_course_price(@RequestParam Long price, @RequestParam Long course_id, HttpSession session,
                                       Model model, RedirectAttributes redirectAttributes) {
        if (authorizationService.isUserAuthorBySession(session) &&
                courseService.userIsAuthorOfCourse(authorizationService.getUserBySession(session).getId(), course_id)) {
            courseService.changeCoursePrice(price, course_id);
            redirectAttributes.addAttribute("courseId", course_id);
            return "redirect:/edit_course";
        } else {
            model.addAttribute("errorMessage", "Not enough rights");
            return "error";
        }
    }
}