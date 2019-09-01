package com.example.fieldforce.controller;

import com.example.fieldforce.entity.FfaUser;
import com.example.fieldforce.repositories.FfaUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class Sample {

    @Autowired private FfaUserRepo ffaUserRepo;

    @GetMapping("/")
    public String getData(){
        return new String("Hello");
    }

    @PostMapping("/add")
    public FfaUser addUser(@RequestBody FfaUser user){
        return ffaUserRepo.save(user);
    }

    @GetMapping("/all")
    public List<FfaUser> getAll(){
        return ffaUserRepo.findAll();
    }
}
