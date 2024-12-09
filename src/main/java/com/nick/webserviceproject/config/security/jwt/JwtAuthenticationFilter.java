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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import reactor.util.annotation.NonNull;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("---- JwtAuthenticationFilter: Start ----");
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Request Method: " + request.getMethod());

        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("Extracted Token: " + token);

            try {
                username = jwtUtils.extractUsername(token);
                System.out.println("Extracted Username from Token: " + username);
            } catch (Exception e) {
                System.out.println("Error extracting username from token: " + e.getMessage());
            }
        } else {
            System.out.println("Authorization header missing or does not start with 'Bearer '");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("No existing authentication found. Validating token...");

            try {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                System.out.println("Loaded UserDetails: " + userDetails.getUsername());
                System.out.println("User Authorities: " + userDetails.getAuthorities());

                if (jwtUtils.validateToken(token, userDetails)) {
                    System.out.println("Token is valid. Setting authentication in SecurityContext...");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Username: " + userDetails.getUsername());
                    System.out.println("Authentication set for user: " + userDetails.getAuthorities());
                } else {
                    System.out.println("Token validation failed");
                }
            } catch (Exception e) {
                System.out.println("Error validating token: " + e.getMessage());
            }
        } else {
            System.out.println("No username extracted or authentication already exists.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println("SecurityContext Authentication: " + authentication.getName());
            System.out.println("SecurityContext Authorities: " + authentication.getAuthorities());
        } else {
            System.out.println("SecurityContext does not contain authentication.");
        }

        System.out.println("---- JwtAuthenticationFilter: End ----");
        filterChain.doFilter(request, response);
    }
}
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            final String token = authHeader.substring(7);
//            final String username = jwtUtils.getUsernameFromJwtToken(token);
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (username != null && authentication == null) {
//                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
//
//                if (jwtUtils.validateJwtToken(token)) {
//                    jwtUtils.getUsernameFromJwtToken(token);
//
//
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails.getUsername(), null, userDetails.getAuthorities()
//                    );
////                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                    authToken.setDetails(token);
//
//                }
//            } filterChain.doFilter(request, response);
//        } catch (Exception e) {
//            handlerExceptionResolver.resolveException(request, response, null, e);
//        }
//    }
//    }

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



