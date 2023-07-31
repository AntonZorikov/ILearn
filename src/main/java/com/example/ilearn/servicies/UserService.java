package com.example.ilearn.servicies;

import com.example.ilearn.entities.UserEntity;
import com.example.ilearn.exceptions.IncorrectLoginOrPassword;
import com.example.ilearn.exceptions.UserAlreadyExist;
import com.example.ilearn.models.LoginInputs;
import com.example.ilearn.models.SigninInputs;
import com.example.ilearn.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity createUser(LoginInputs loginInputs) throws UserAlreadyExist {
        if(userRepository.findByLogin(loginInputs.getLogin()) == null &&
                userRepository.findByEmail(loginInputs.getEmail()) == null){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            loginInputs.setPassword(bCryptPasswordEncoder.encode(loginInputs.getPassword()));
            UserEntity user = new UserEntity(loginInputs);
            user.setRole("USER");
            return userRepository.save(user);
        }
        else{
            throw new UserAlreadyExist("User Already Exist");
        }
    }

    public UserEntity signin(SigninInputs signinInputs) throws IncorrectLoginOrPassword {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(userRepository.findByLogin(signinInputs.getLogin()) != null &&
                bCryptPasswordEncoder.matches(signinInputs.getPassword(),
                        userRepository.findByLogin(signinInputs.getLogin()).getPassword())){
            return userRepository.findByLogin(signinInputs.getLogin());
        }
        else{
            throw new IncorrectLoginOrPassword("Incorrect login or password");
        }
    }
}