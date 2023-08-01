package com.example.mutsaMarket.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FormController {
    @GetMapping("/form/login-form")
    public String loginForm(){
        return "login-form";
    }

    @GetMapping("/form/register-form")
    public String registerForm(){
        return "register-form";
    }

    @GetMapping("/form/writing-article-form")
    public String writingForm(){
        return "writing-article-form";
    }
}
