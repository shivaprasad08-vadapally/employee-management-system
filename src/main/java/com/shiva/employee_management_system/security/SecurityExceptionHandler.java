package com.shiva.employee_management_system.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.shiva.employee_management_system.exception.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityExceptionHandler
        implements AuthenticationEntryPoint, AccessDeniedHandler {

    // =========================================================
    // 401 UNAUTHORIZED
    // =========================================================

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        ApiErrorResponse error = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "Authentication is required to access this resource",
                null
        );

        writeResponse(response, error);
    }

    // =========================================================
    // 403 FORBIDDEN
    // =========================================================

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {

        ApiErrorResponse error = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpServletResponse.SC_FORBIDDEN,
                "Forbidden",
                "You do not have permission to access this resource",
                null
        );

        writeResponse(response, error);
    }

    // =========================================================
    // WRITE JSON RESPONSE
    // =========================================================

    private void writeResponse(
            HttpServletResponse response,
            ApiErrorResponse error)
            throws IOException {

        response.setStatus(error.getStatus());
        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );

        String json = """
                {
                    "timestamp": "%s",
                    "status": %d,
                    "error": "%s",
                    "message": "%s",
                    "details": null
                }
                """.formatted(
                        error.getTimestamp(),
                        error.getStatus(),
                        error.getError(),
                        error.getMessage()
                );

        response.getWriter().write(json);
    }
}