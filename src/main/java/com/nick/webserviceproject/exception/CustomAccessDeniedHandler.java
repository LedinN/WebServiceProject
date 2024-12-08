package com.nick.webserviceproject.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        System.out.println("---- Access Denied Handler Triggered ----");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("HTTP Method: " + request.getMethod());
        System.out.println("Remote Address: " + request.getRemoteAddr());
        System.out.println("User Principal: " + (request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "None"));
        System.out.println("Error Message: " + accessDeniedException.getMessage());

        // Return the 403 Forbidden response
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
    }
}