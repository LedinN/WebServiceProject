package com.nick.webserviceproject.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        System.out.println("AuthenticationEntryPoint triggered");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Error: " + authException.getMessage());

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication is required to access this resource");
    }
}