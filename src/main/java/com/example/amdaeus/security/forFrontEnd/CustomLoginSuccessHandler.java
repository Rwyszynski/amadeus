package com.example.amdaeus.security.forFrontEnd;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        System.out.println(">>> LOGOWANIE UDANE: " + authentication.getName());

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());

        if (authentication.getName().equalsIgnoreCase("admin@example.com")) {
            updatedAuthorities.add(new SimpleGrantedAuthority("ADMIN_ROLE"));
        }

        Authentication newAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                updatedAuthorities
        );

        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(newAuth);
        response.sendRedirect("/admin-btrs");
    }
}
