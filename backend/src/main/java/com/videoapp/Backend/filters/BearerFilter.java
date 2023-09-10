package com.videoapp.Backend.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

public class BearerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if any
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }


        Cookie[] cookies = httpRequest.getCookies();

        // Use a single-element array to hold the jwtToken
        String[] jwtTokenHolder = new String[1];

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {
                    jwtTokenHolder[0] = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtTokenHolder[0] != null) {
            HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
                @Override
                public Enumeration<String> getHeaders(String name) {
                    if ("Authorization".equalsIgnoreCase(name)) {
                        return Collections.enumeration(Collections.singletonList("Bearer " + jwtTokenHolder[0]));
                    }
                    return super.getHeaders(name);
                }

                @Override
                public String getHeader(String name) {
                    if ("Authorization".equalsIgnoreCase(name)) {
                        return "Bearer " + jwtTokenHolder[0];
                    }
                    return super.getHeader(name);
                }
            };
            chain.doFilter(wrappedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Cleanup code, if anya
    }
}
