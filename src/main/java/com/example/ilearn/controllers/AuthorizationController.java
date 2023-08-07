package com.example.ilearn.controllers;

import com.example.ilearn.entities.UserEntity;
import com.example.ilearn.exceptions.IncorrectLoginOrPassword;
import com.example.ilearn.exceptions.UserAlreadyExist;
import com.example.ilearn.models.LoginInputs;
import com.example.ilearn.models.SigninInputs;
import com.example.ilearn.servicies.AuthorizationService;
import com.example.ilearn.servicies.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthorizationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public String login(@ModelAttribute LoginInputs loginInputs, Model model, HttpSession session){
        try {
            UserEntity user = userService.createUser(loginInputs);
            authorizationService.addUserInfoToSession(session, user);
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "authorization";
        }
        catch(UserAlreadyExist e){
            model.addAttribute("error", true);
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "login";
        }
    }

    @PostMapping("/signin")
    public String signin(@ModelAttribute SigninInputs signinInputs, Model model, HttpSession session){
        try{
            UserEntity user = userService.signin(signinInputs);
            authorizationService.addUserInfoToSession(session, user);
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "authorization";
        }
        catch(IncorrectLoginOrPassword e){
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "Incorrect login or password");
            model.addAttribute("isAuthorize", authorizationService.isSessionActive(session));
            return "signin";
        }
    }
}
