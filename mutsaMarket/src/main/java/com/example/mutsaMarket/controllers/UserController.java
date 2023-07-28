package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dto.UserLoginDto;
import com.example.mutsaMarket.dto.UserRegisterDto;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.UserService;
import com.example.mutsaMarket.userManage.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRegisterDto userRegisterDto){
        ResponseObject response = new ResponseObject();

        if(!userRegisterDto.getUserPassword().equals(userRegisterDto.getPasswordCheck())) {
            response.setMessage("비밀번호 중복확인을 해주세요.");
            return ResponseEntity.badRequest().body(response);
        }
        else{
            userService.createUser
                    (CustomUserDetails.builder()
                            .userId(userRegisterDto.getUserId())
                            .userPassword(passwordEncoder.encode(userRegisterDto.getUserPassword()))
                            .phone(userRegisterDto.getPhone())
                            .email(userRegisterDto.getEmail())
                            .address(userRegisterDto.getAddress())
                            .build());

            response.setMessage("회원가입이 완료되었습니다.");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDto userLoginDto){
        String token = userService.login(userLoginDto);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/get-info")
    public void profile(Authentication authentication){
        String username = authentication.getName();
        CustomUserDetails user = (CustomUserDetails) userService.profile(username);
        log.info(user.getUsername());
        log.info(user.getPassword());
        log.info(user.getPhone());
        log.info(user.getEmail());
        log.info(user.getAddress());
    }
}
