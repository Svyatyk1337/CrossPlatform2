package com.example.demo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                       Authentication authentication) throws ServletException, IOException {
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        String redirectURL = request.getContextPath();
        
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            redirectURL += "/admin/dashboard";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            redirectURL += "/transfers";
        } else {
            redirectURL += "/";
        }
        
        response.sendRedirect(redirectURL);
    }
}