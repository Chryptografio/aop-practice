package com.example.aop.practice.util;

import com.example.aop.practice.model.User;

import java.util.Arrays;
import java.util.List;

public class RegisteredUsers {
    public static final List<User> USERS = Arrays.asList(
            new User("James", "password"),
            new User("John", "pass123")
    );
}
