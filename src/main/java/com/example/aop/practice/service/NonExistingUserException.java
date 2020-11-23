package com.example.aop.practice.service;

import com.example.aop.practice.model.User;

public class NonExistingUserException extends RuntimeException {
    public NonExistingUserException() {
    }

    public NonExistingUserException(String message) {
        super(message);
    }

    public NonExistingUserException(String name, String password) {
        super("Name: " + name + "; Password: " + password);
    }

    public NonExistingUserException(User user) {
        this(user.getName(), user.getPassword());
    }
}
