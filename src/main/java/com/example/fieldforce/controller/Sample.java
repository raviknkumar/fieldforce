package com.example.fieldforce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Sample {

    @GetMapping("/")
    public String getData(){
        return new String("Hello");
    }
}
