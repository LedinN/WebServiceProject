package com.nick.webserviceproject.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTimeMillis = currentTimeMillis + jwtExpirationMs;

        System.out.println("Current time millis: " + currentTimeMillis);
        System.out.println("JWT expiration delay (ms): " + jwtExpirationMs);
        System.out.println("Expiration time millis: " + expirationTimeMillis);
        System.out.println("Current time: " + new Date(currentTimeMillis));
        System.out.println("Expiration time: " + new Date(expirationTimeMillis));

        System.out.println("Data to generateJwtToken: " + username + roles);
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
        System.out.println("Expected roles from JWT" + claims.get("roles", List.class));
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

   /* private Key createSigningKey() {
        if (secretKey.length() >= 32) {
            System.out.println("Using provided secret key.");
            return Keys.hmacShaKeyFor(secretKey.getBytes());
        } else {
            System.out.println("Generating secure key as fallback.");
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
    }*/
}