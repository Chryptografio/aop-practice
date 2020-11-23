package com.example.aop.practice.advice;

import com.example.aop.practice.advice.Capturer;
import com.example.aop.practice.model.User;
import com.example.aop.practice.service.BadCredentialsException;
import com.example.aop.practice.service.DummyAuthenticatorService;
import com.example.aop.practice.service.NonExistingUserException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.stream.Stream;

import static com.example.aop.practice.advice.DummyAuthenticatorServiceTest.Config;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {Config.class, BadCredentialsLogger.class, DummyAuthenticatorService.class})
class DummyAuthenticatorServiceTest {

    @MockBean
    private Capturer capturer;

    @Autowired
    private ApplicationContext applicationContext;

    private static final ExceptionVerifier<DummyAuthenticatorService, User> BAD_CREDENTIALS_EXCEPTION =
            ((dummyAuthenticatorService, user) -> assertThatThrownBy(() -> dummyAuthenticatorService.authenticateUser(user))
                    .isExactlyInstanceOf(BadCredentialsException.class));

    private static final ExceptionVerifier<DummyAuthenticatorService, User> NON_EXISTING_USER_EXCEPTION =
            ((dummyAuthenticatorService, user) -> assertThatThrownBy(() -> dummyAuthenticatorService.authenticateUser(user))
                    .isExactlyInstanceOf(NonExistingUserException.class));

    private static final ExceptionVerifier<DummyAuthenticatorService, User> NO_EXCEPTION =
            (((dummyAuthenticatorService, user) -> assertThatCode(() -> dummyAuthenticatorService.authenticateUser(user))
                    .doesNotThrowAnyException()));

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void authenticateUser(
            final User user,
            final ExceptionVerifier<DummyAuthenticatorService, User> exceptionVerifier,
            final int badCredentialsTimes,
            final int nonExistingUserTimes) {

        final DummyAuthenticatorService authenticatorService = applicationContext.getBean(DummyAuthenticatorService.class);

        exceptionVerifier.verify(authenticatorService, user);

        verify(capturer, times(badCredentialsTimes)).captureWrongPassword();
        verify(capturer, times(nonExistingUserTimes)).captureNonExistingUser();

    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                Arguments.of(new User("James", "password"), NO_EXCEPTION, 0, 0),
                Arguments.of(new User("John", "pass123"), NO_EXCEPTION, 0, 0),
                Arguments.of(new User("Sarah", "1234"), NON_EXISTING_USER_EXCEPTION, 0, 1),
                Arguments.of(new User("James", "1111"), BAD_CREDENTIALS_EXCEPTION, 1, 0),
                Arguments.of(new User("Andy", "password"), NON_EXISTING_USER_EXCEPTION, 0, 1)
        );
    }

    @TestConfiguration
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    static class Config {

    }

    @FunctionalInterface
    interface ExceptionVerifier<T, U> {
        void verify(T t, U u);
    }
}