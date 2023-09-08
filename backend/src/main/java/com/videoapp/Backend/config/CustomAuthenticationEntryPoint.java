package com.videoapp.Backend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Set the cookie to clear the JWT
        response.setHeader("Set-Cookie", "JWT=; Domain=localhost; Path=/; Max-Age=1; SameSite=None; Secure; HttpOnly");

        // Send an unauthorized error response (you can customize this further if needed)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "CUSTOM ENTRY POINT");
    }
}