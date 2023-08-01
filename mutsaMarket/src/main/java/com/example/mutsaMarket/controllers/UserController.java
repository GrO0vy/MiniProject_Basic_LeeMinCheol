package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dto.UserRegisterDto;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.UserService;
import com.example.mutsaMarket.userManage.CustomUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody UserRegisterDto userRegisterDto){
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
    public ResponseEntity login(@NotBlank @RequestParam("inputId") String inputId,
                                @NotBlank @RequestParam("inputPw") String inputPw){
        String token = userService.login(inputId, inputPw);

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

    @ExceptionHandler({DataIntegrityViolationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity dataIntegrityViolationError(Exception exception){
        log.error("모든 항목을 입력하지 않음");

        ResponseObject response = new ResponseObject();
        response.setMessage("필수 항목을 모두 입력해주세요");

        return ResponseEntity.badRequest().body(response);
    }
}
