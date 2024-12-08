package com.nick.webserviceproject.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private Key signingKey;

    public String generateJwtToken(String username, List<String> roles) {

        System.out.println("Data to generateJwtToken: " + username + roles);
        System.out.println("Username in login: " + username);
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            System.out.println("VALIDATE TOKEN Token expiry:" + extractAllClaims(token).getExpiration());
            return true;
        } catch (Exception e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    public String getUsernameFromJwtToken(String token) {
        return extractClaim(token, Claims::getSubject);

    }

    public List<String> getRolesFromJwtToken(String token) {
        Claims claims = extractAllClaims(token);
        System.out.println("Expected roles from JWT" + claims.get("roles", List.class).toString());
        return claims.get("roles", List.class);

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private synchronized Key getSigningKey() {
        if (signingKey == null) {
            signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            System.out.println("Signing key generated: "+signingKey);
        }
        return signingKey;
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