package com.example.amdaeus.service;

import com.example.amdaeus.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JwtService {

    private static final String SECRET_KEY = "MYEXTREMELYSECRETKEYFORJWTGENERATIONWHICHSHOULDNOTBESHARED";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmailAddress())
                .claim("roles", user.getUserType().stream().map(Enum::name).collect(Collectors.toSet()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Set<String> extractRoles(String token) {
        Object rolesObj = extractAllClaims(token).get("roles");
        if (rolesObj instanceof Iterable<?> rolesIterable) {
            return StreamSupport.stream(rolesIterable.spliterator(), false)
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
