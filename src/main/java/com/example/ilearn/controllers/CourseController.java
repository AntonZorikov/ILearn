package com.example.ilearn.controllers;

import com.example.ilearn.entities.CourseEntity;
import com.example.ilearn.entities.LessonEntity;
import com.example.ilearn.exceptions.*;
import com.example.ilearn.models.CreateCourseInputs;
import com.example.ilearn.models.CreateLessonInputs;
import com.example.ilearn.servicies.AuthorizationService;
import com.example.ilearn.servicies.CourseService;
import com.example.ilearn.servicies.PaymentService;
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

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create_course")
    private String createCourse(CreateCourseInputs createCourseInputs, Model model, HttpSession session) throws Exception {
        try {
            if (authorizationService.isSessionActive(session)) {
                courseService.createCourse(createCourseInputs, session);
                model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
                return "home";
            } else {
                model.addAttribute("error", true);
                model.addAttribute("errorMessage", "Not enough rights");
                model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
                return "error";
            }
        } catch (CourseAlreadyExist | UserNotFound e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
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
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
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
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "error";
        }
    }

    @PostMapping("/change_lesson_title")
    private String change_lesson_title(@RequestParam String title, @RequestParam Long lessonId,HttpSession session,
                                       Model model, RedirectAttributes redirectAttributes){
        LessonEntity lesson = courseService.getLessonById(lessonId);
        if (authorizationService.isUserAuthorBySession(session) &&
                courseService.userIsAuthorOfCourse(authorizationService.getUserBySession(session).getId(), lesson.getCourse_id())) {
            courseService.changeLessonTitle(title, lessonId);
            redirectAttributes.addAttribute("lessonId", lesson.getId());
            return "redirect:/edit_lesson";
        } else {
            model.addAttribute("errorMessage", "Not enough rights");
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "error";
        }
    }

    @PostMapping("/change_lesson_text")
    private String change_lesson_text(@RequestParam String text, @RequestParam Long lessonId,HttpSession session,
                                       Model model, RedirectAttributes redirectAttributes){
        LessonEntity lesson = courseService.getLessonById(lessonId);
        if (authorizationService.isUserAuthorBySession(session) &&
                courseService.userIsAuthorOfCourse(authorizationService.getUserBySession(session).getId(), lesson.getCourse_id())) {
            courseService.changeLessonText(text, lessonId);
            redirectAttributes.addAttribute("lessonId", lesson.getId());
            return "redirect:/edit_lesson";
        } else {
            model.addAttribute("errorMessage", "Not enough rights");
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "error";
        }
    }

    @PostMapping("buy_course")
    private String buy_course(@RequestParam Long courseId, HttpSession session, Model model, RedirectAttributes redirectAttributes){
        paymentService.buyCourse(authorizationService.getUserBySession(session).getId(), courseId);
        redirectAttributes.addAttribute("courseId", courseId);
        return "redirect:/course";
    }

    @PostMapping("/change_course_description")
    private String change_course_description(@RequestParam String description, @RequestParam Long courseId,HttpSession session,
                                      Model model, RedirectAttributes redirectAttributes){
        if (authorizationService.isUserAuthorBySession(session) &&
                courseService.userIsAuthorOfCourse(authorizationService.getUserBySession(session).getId(), courseId)) {
            courseService.changeCourseDescription(description, courseId);
            redirectAttributes.addAttribute("courseId", courseId);
            return "redirect:/edit_course";
        } else {
            model.addAttribute("errorMessage", "Not enough rights");
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "error";
        }
    }
}