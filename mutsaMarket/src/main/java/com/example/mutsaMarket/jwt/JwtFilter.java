package com.example.mutsaMarket.jwt;

import com.example.mutsaMarket.userManage.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    public JwtFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException
    {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.split(" ")[1];

            if(jwtUtils.isValidatedToken(token)){
                String userId = jwtUtils.parseJwt(token).getSubject();

                SecurityContext context = SecurityContextHolder.createEmptyContext();

                AbstractAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(
                        CustomUserDetails.builder()
                                .userId(userId)
                                .build(),
                        token, new ArrayList<>()
                );

                context.setAuthentication(authenticationToken);

                SecurityContextHolder.setContext(context);
            }
            else log.warn("token is not valid");
        }
        else log.warn("not exist authorization header");

        filterChain.doFilter(request, response);
    }
}
