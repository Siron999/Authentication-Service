package com.microservice.authentication.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.authentication.entity.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("inside commence");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message;

        final String exception = (String) request.getAttribute("exception");

        if (authException.getCause() != null) {
            message = authException.getCause().toString() + " " + authException.getMessage();
        } else {
            message = authException.getMessage();
        }
        ErrorMessage errorMessage;
        if (exception != null) {
            errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED, exception);
        } else {
            errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED, message);
        }
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorMessage));
    }
}
