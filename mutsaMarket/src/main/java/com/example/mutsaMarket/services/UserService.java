package com.example.mutsaMarket.services;


import com.example.mutsaMarket.dto.UserLoginDto;
import com.example.mutsaMarket.entity.UserEntity;
import com.example.mutsaMarket.jwt.JwtUtils;
import com.example.mutsaMarket.repositories.UserRepository;
import com.example.mutsaMarket.userManage.CustomUserDetails;
import com.example.mutsaMarket.userManage.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsManager manager;

    public void createUser(UserDetails user) {
        // CustomUserDetailsManager 에서 처리
        manager.createUser(user);
    }

    public String login(UserLoginDto userLoginDto){
        String inputId = userLoginDto.getUserId();
        String inputPw = userLoginDto.getUserPassword();

        if(!manager.userExists(inputId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        UserDetails user = manager.loadUserByUsername(inputId);

        if(!passwordEncoder.matches(inputPw, user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);


        String token = jwtUtils.generateToken(user);

        return token;
    }

    public UserDetails profile(String userId){
        return manager.loadUserByUsername(userId);
    }

}
