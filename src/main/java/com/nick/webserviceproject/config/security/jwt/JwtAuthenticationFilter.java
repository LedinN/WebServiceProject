package com.nick.webserviceproject.config.security.jwt;

import com.nick.webserviceproject.config.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.annotation.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull  HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    )
            throws ServletException, IOException
    {
        System.out.println("---- JwtAuthenticationFilter Start ----");
        System.out.println("Request URI: " + request.getRequestURI());
    String token = extractJwtFromCookie(request);
        System.out.println("Extracted Token: " + token);

    if (token != null && jwtUtils.validateJwtToken(token)) {
        System.out.println("Token is valid");
        String username = jwtUtils.getUsernameFromJwtToken(token);
        System.out.println("Extracted Username from Token: " + username);
        List<String> roles = jwtUtils.getRolesFromJwtToken(token);
        System.out.println("Extracted Roles from Token: " + roles);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("No existing authentication. Setting up new authentication context.");
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            System.out.println("Loaded UserDetails: " + userDetails);

            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            System.out.println("Mapped Authorities: " + authorities);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username, null, authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("Authentication set in SecurityContextHolder");
            System.out.println("Authenticated user: "+ username);
            System.out.println("Authorities: "+ authorities);
        }
        else {
            System.out.println("Invalid or Missing Token");
        }
    }
        System.out.println("---- JwtAuthenticationFilter End ----");
    filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        System.out.println("Extracting JWT from Cookies...");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie Name: " + cookie.getName() + ", Value: " + cookie.getValue());
                if ("authToken".equals(cookie.getName())) {
                    System.out.println("Found authToken cookie");
                    return cookie.getValue();
                }
            }
        }
        System.out.println("authToken cookie not found");
        return null;
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        System.out.println("Extracting JWT from Authorization Header...");
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("Authorization Header: " + header);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
