package com.example.fieldforce.serviceImpl;

import com.example.fieldforce.converter.UserConverter;
import com.example.fieldforce.entity.FfaUser;
import com.example.fieldforce.model.FfaUserDto;
import com.example.fieldforce.repositories.UserRepo;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.example.fieldforce.exception.*;

import javax.validation.ConstraintViolationException;

@Service
public class UserService {

    @Autowired private UserConverter userConverter;
    @Autowired private UserRepo userRepo;

    public FfaUserDto addUser(FfaUserDto ffaUserDto) throws FfaException{
        try {
            FfaUser ffaUser = userConverter.convertModelToEntity(ffaUserDto);
            FfaUser user = userRepo.save(ffaUser);
            return userConverter.convertEntityToModel(user);
        }
        catch(DataIntegrityViolationException exc){
            throw new FfaException("USER_ALREADY_EXISTS","Sorry, user name already exists");
        }
    }

    public FfaUserDto handleLogin(FfaUserDto userDto){
        FfaUser ffaUser = userRepo.findByName(userDto.getName());
        if(ffaUser == null){
            throw new FfaException("NO_USER_FOUND", "So, you don't have an account, please signUp");
        }
        if(ffaUser.getPassword().equals(userDto.getPassword())){
            return userConverter.convertEntityToModel(ffaUser);
        }
        throw new FfaException("ERROR", "UserName or password is invalid");
    }
}

