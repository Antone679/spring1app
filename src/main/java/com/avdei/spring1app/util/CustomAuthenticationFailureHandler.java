package com.avdei.spring1app.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "Неверный логин";
        } else if (exception.getMessage().contains("Bad credentials")) {
            errorMessage = "Неверный пароль";
        } else {
            errorMessage = "Ошибка аутентификации";
        }

        request.getSession().setAttribute("error", errorMessage);
        response.sendRedirect("/auth/login?error");
    }
}
