package com.uniroma3.prog.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String referer = request.getParameter("referer");
        if (referer != null && !referer.contains("/login") && !referer.contains("/register")) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect("/");
        }
    }
}