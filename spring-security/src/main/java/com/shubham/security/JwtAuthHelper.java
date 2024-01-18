package com.shubham.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtAuthHelper {
    public static final long JWT_TOKEN_VALIDITY = 1*60*60;
    private static final String secret = "yrhbedeyr7384t4i32urgbfdhjgri47ry347i8yuiregfhjdsbfr823t7ri8734yugfdshjbwryiegfudsbhf";
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    public Date getExpirationTimeFromToken(String token){
        return getClaimsFromToken(token, Claims::getExpiration);
    }
    public Boolean isTokenExpired(String token){
        final Date expirationTimeFromToken = getExpirationTimeFromToken(token);
        return expirationTimeFromToken.before(new Date());
    }
    private <T> T  getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }


    public String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        return generateAuthToken(claims, subject);
    }

    private String generateAuthToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, secret).compact();

    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
