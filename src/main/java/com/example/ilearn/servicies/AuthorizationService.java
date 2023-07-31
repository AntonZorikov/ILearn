package com.example.ilearn.servicies;

import com.example.ilearn.entities.UserEntity;
import com.example.ilearn.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    public void addUserInfoToSession(HttpSession httpSession, UserEntity user){
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("sessionIsActive", true);
    }

    public boolean isSessionActive(HttpSession session){
        if(session.getAttribute("sessionIsActive") == null){
            return false;
        }
        else {
            return (boolean) session.getAttribute("sessionIsActive");
        }
    }
    public boolean isUserAuthorBySession(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return false;
        }

        Optional<UserEntity> user = userRepository.findById(userId);
        return user.isPresent() && Objects.equals(user.get().getRole(), "AUTHOR");
    }


    public UserEntity getUserBySession(HttpSession session){
        Optional<UserEntity> user = userRepository.findById((Long) session.getAttribute("userId"));
        return user.orElse(null);
    }
}
