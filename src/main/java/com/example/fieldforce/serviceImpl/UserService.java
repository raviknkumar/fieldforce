package com.example.fieldforce.serviceImpl;

import com.example.fieldforce.converter.UserConverter;
import com.example.fieldforce.entity.FfaUser;
import com.example.fieldforce.model.FfaUserDto;
import com.example.fieldforce.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.fieldforce.exception.*;

@Service
public class UserService {

    @Autowired private UserConverter userConverter;
    @Autowired private UserRepo userRepo;

    public FfaUserDto addUser(FfaUserDto ffaUserDto){
        FfaUser ffaUser = userConverter.convertModelToEntity(ffaUserDto);
        FfaUser user = userRepo.save(ffaUser);
        return userConverter.convertEntityToModel(user);
    }

    public FfaUserDto handleLogin(FfaUserDto userDto){
        FfaUser ffaUser = userRepo.findByName(userDto.getName());
        if(ffaUser.getPassword().equals(userDto.getPassword())){
            return userConverter.convertEntityToModel(ffaUser);
        }
        throw new FfaException(ErrorCode.CLIENT_ERROR, "Not a valid user");
    }
}

