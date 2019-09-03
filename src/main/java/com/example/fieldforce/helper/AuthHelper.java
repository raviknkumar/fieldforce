package com.example.fieldforce.helper;


import com.example.fieldforce.model.AuthUser;

public class AuthHelper {

    public static AuthUser getAuthUser(String id){
        Integer userId = Integer.parseInt(id);
        return AuthUser.builder()
                .id(userId)
                .build();
    }

}
