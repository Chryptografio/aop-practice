package com.example.aop.practice.service;

import com.example.aop.practice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.aop.practice.util.RegisteredUsers.USERS;

@Slf4j
@Service
public class DummyAuthenticatorService {

    public void authenticateUser(User user) {
        final long count = USERS.stream()
                .filter(user1 -> user1.getName().equals(user.getName()))
                .count();
        if (count == 0) {
            throw new NonExistingUserException(user);
        }
        if (!USERS.contains(user)) {
            throw new BadCredentialsException(user);
        }
        log.info("{} was authenticated", user.getName());
    }
}
