package com.example.mutsaMarket.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;

@Component
@Slf4j
public class JwtUtils {
    private final Key signinKey;
    private final JwtParser jwtParser;

    public JwtUtils(@Value("${jwt.secret}") String jwtSecret){
        this.signinKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(signinKey)
                .build();
    }

    public String generateToken(UserDetails userDetails){
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)));

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signinKey)
                .compact();
    }

    public boolean isValidatedToken(String token){
        try{
            jwtParser.parseClaimsJws(token);
            return true;
        }catch (Exception e){
            log.warn("jwt token parsing Error");
            return false;
        }
    }

    public Claims parseJwt(String token){
        return jwtParser.parseClaimsJws(token).getBody();
    }
}
