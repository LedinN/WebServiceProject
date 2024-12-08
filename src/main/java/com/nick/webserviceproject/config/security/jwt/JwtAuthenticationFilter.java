package com.nick.webserviceproject.config.security.jwt;

import com.nick.webserviceproject.config.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import reactor.util.annotation.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("---- JwtAuthenticationFilter Start ----");
        System.out.println("Request URI: " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = authHeader.substring(7);
            final String username = jwtUtils.getUsernameFromJwtToken(token);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                if (jwtUtils.validateJwtToken(token)) {
                    jwtUtils.getUsernameFromJwtToken(token);


                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(), null, userDetails.getAuthorities()
                    );
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    authToken.setDetails(token);

                }
            } filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
    }

//        if (token != null && jwtUtils.validateJwtToken(token)) {
//            System.out.println("Token is valid");
//            String username = jwtUtils.getUsernameFromJwtToken(token);
//            System.out.println("Extracted Username from Token: " + username);
//            List<String> roles = jwtUtils.getRolesFromJwtToken(token);
//            System.out.println("Extracted Roles from Token: " + roles);
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                System.out.println("No existing authentication. Setting up new authentication context.");
//                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
//                System.out.println("Loaded UserDetails: " + userDetails.getUsername());
//
//                List<GrantedAuthority> authorities = roles.stream()
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//                System.out.println("Mapped Authorities: " + authorities);
//
//
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails.getUsername(), null, authorities
//                );
//                authToken.setDetails(token);
//
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//                System.out.println("Authentication set in SecurityContextHolder");
//                System.out.println("Authenticated user: " + userDetails.getUsername());
//                System.out.println("Authorities: " + userDetails.getAuthorities());
//            } else {
//                System.out.println("Invalid or Missing Token");
//            }
//        }
//
//        System.out.println("Final Security Context: " + SecurityContextHolder.getContext().getAuthentication());
//        System.out.println("---- JwtAuthenticationFilter End ----");
//        filterChain.doFilter(request, response);
//    }



