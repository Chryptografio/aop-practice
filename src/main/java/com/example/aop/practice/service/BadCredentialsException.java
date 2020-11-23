package com.example.aop.practice.service;

import com.example.aop.practice.model.User;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
    }

    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException(String name, String password) {
        super("Name: " + name + "; Password: " + password);
    }

    public BadCredentialsException(User user) {
        this(user.getName(), user.getPassword());
    }
}
