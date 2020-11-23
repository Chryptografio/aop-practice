package com.example.aop.practice.advice;

import com.example.aop.practice.model.User;
import com.example.aop.practice.service.BadCredentialsException;
import com.example.aop.practice.service.NonExistingUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class BadCredentialsLogger {

    private final Capturer capturer;

    @AfterThrowing(pointcut = "execution(* com.example.aop.practice.service.DummyAuthenticatorService.authenticateUser(..))", throwing = "exception")
    public void captureWrongPassword(JoinPoint joinPoint, BadCredentialsException exception) {
        final User user = (User) joinPoint.getArgs()[0];
        log.info("User {} tried to access account", user.getName());
        capturer.captureWrongPassword();
    }

    @AfterThrowing(pointcut = "execution(* com.example.aop.practice.service.DummyAuthenticatorService.authenticateUser(..))", throwing = "exception")
    public void captureNonExistingUser(NonExistingUserException exception) {
        log.info("There was an attempt to login to the server");
        capturer.captureNonExistingUser();
    }
}
