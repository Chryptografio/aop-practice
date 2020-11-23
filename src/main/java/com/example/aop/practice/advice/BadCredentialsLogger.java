package com.example.aop.practice.advice;

import com.example.aop.practice.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class BadCredentialsLogger {
    
    // TODO: make changes here

    private final Capturer capturer;

    public void captureWrongPassword(JoinPoint joinPoint) {
        final User user = (User) joinPoint.getArgs()[0];
        log.info("User {} tried to access account", user.getName());
        capturer.captureWrongPassword();
    }

    public void captureNonExistingUser() {
        log.info("There was an attempt to login to the server");
        capturer.captureNonExistingUser();
    }
}
