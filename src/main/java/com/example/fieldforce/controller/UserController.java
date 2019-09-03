package com.example.fieldforce.controller;

import com.example.fieldforce.entity.FfaUser;
import com.example.fieldforce.exception.FfaException;
import com.example.fieldforce.model.ApiResponse;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.FfaUserDto;
import com.example.fieldforce.serviceImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    private ApiResponse<FfaUserDto> addUser(@RequestBody FfaUserDto userDto) throws FfaException {
        FfaUserDto ffaUserDto = userService.addUser(userDto);
        return new ApiResponse<>(ffaUserDto);
    }

    @PostMapping("/login")
    private ApiResponse<FfaUserDto> login(@RequestBody FfaUserDto userDto) throws FfaException {
        FfaUserDto ffaUserDto = userService.handleLogin(userDto);
        return new ApiResponse<>(ffaUserDto);
    }

}
