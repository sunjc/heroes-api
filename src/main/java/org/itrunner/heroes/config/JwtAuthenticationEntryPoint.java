package org.itrunner.heroes.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.itrunner.heroes.exception.ErrorMessage;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
        Exception exception = (Exception) request.getAttribute("exception");

        if (exception != null) {
            response.setStatus(UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            byte[] b = new ObjectMapper().writeValueAsBytes(new ErrorMessage(exception));
            response.getOutputStream().write(b);
        } else {
            response.sendError(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase());
        }
    }
}