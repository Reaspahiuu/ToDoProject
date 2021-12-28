package com.example.todoproject.controller;

import com.example.todoproject.model.User;
import com.example.todoproject.model.UserLoginModel;
import com.example.todoproject.model.UserSession;
import com.example.todoproject.repository.UserRepository;
import com.example.todoproject.repository.UserSessionRepository;
import com.example.todoproject.service.UtilityService;
import org.aspectj.weaver.bcel.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/todo")
public class AuthenticationController {
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final UtilityService utilityService;

    public AuthenticationController(UserRepository userRepository, UtilityService utilityService, UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.utilityService = utilityService;
    }

    @GetMapping("/register-user")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register-user")
    public String postRegister(@ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
        this.userRepository.save(user);
        return "dashboard";
    }

    @GetMapping("/login")
    public String getLogin(Model model,  HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(utilityService.isUserLoggedIn(request.getCookies())){
            response.sendRedirect("/todo/dashboard");
            return null;
        }
        model.addAttribute("user", new UserLoginModel());
        model.addAttribute("loginUser", "/todo/login");
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute UserLoginModel userLoginModel, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User loggedUser = this.userRepository.findByEmailAndPassword
                (userLoginModel.getUsername(), userLoginModel.getPassword());
        if (loggedUser == null) {
            model.addAttribute("user", new UserLoginModel());
            model.addAttribute("loginUser", "/todo/login");
            return "login";
        }
        Cookie cookie = new Cookie("logged_in", "true");
        cookie.setPath("/");
        response.addCookie(cookie);
        UserSession userSession = new UserSession();
        userSession.setUser(loggedUser);
        userSession.setSessionHashCode(utilityService.generateRandomString(20));
        userSession.setIpAddress(request.getRemoteAddr());
        userSessionRepository.save(userSession);
        Cookie sessionCookie = new Cookie("session_id", userSession.getSessionHashCode());
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
        response.sendRedirect("/todo/dashboard");
        return null;
    }

    @GetMapping("/dashboard")
    public String getDashboard(){
        return "dashboard";
    }


}
