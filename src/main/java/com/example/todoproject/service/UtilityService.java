package com.example.todoproject.service;


import com.example.todoproject.model.User;
import com.example.todoproject.model.UserSession;
import com.example.todoproject.repository.UserRepository;
import com.example.todoproject.repository.UserSessionRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

@Service
public class UtilityService {
    private UserRepository userRepository;
    private UserSessionRepository userSessionRepository;

    public UtilityService(UserRepository userRepository, UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
    }

    public boolean isUserLoggedIn(Cookie[] cookies) {
        return this.getLoggedInUser(cookies) != null;
    }

    private User getLoggedInUser(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        Optional<Cookie> cookieOptional = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("logged_in")
                        && c.getValue().equals("true"))
                .findFirst();

        if (!cookieOptional.isPresent()) {
            return null;
        }

        Optional<Cookie> sessionCookieOptional = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("session_id"))
                .findFirst();

        if (!sessionCookieOptional.isPresent()) {
            return null;
        }

        UserSession userSession = this.userSessionRepository.findBySessionHashCode(sessionCookieOptional.get().getValue());
        ;
        if (userSession == null) {
            return null;
        }
        return userSession.getUser();
    }

    public String generateRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

}