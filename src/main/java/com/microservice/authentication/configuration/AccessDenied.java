package com.microservice.authentication.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.authentication.entity.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class AccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message;

        final String exception = (String) request.getAttribute("exception");

        if (accessDeniedException.getCause() != null) {
            message = accessDeniedException.getCause().toString() + " " + accessDeniedException.getMessage();
        } else {
            message = accessDeniedException.getMessage();
        }

        ErrorMessage errorMessage;
        if (exception != null) {
            errorMessage = new ErrorMessage(HttpStatus.FORBIDDEN, exception);
        } else {
            errorMessage = new ErrorMessage(HttpStatus.FORBIDDEN, message);
        }
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorMessage));
    }
}
