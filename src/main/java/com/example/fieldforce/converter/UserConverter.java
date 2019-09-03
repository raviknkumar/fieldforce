package com.example.fieldforce.converter;

import com.example.fieldforce.entity.FfaUser;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.FfaUserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserConverter implements Converter<FfaUser, FfaUserDto> {
    @Override
    public FfaUser convertModelToEntity(FfaUserDto model, AuthUser user) {
        return null;
    }

    @Override
    public FfaUser convertModelToEntity(FfaUserDto model) {
        FfaUser ffaUser = FfaUser.builder()
                .name(model.getName())
                .password(model.getPassword())
                .roles(model.getRoles())
                .build();

        ffaUser.setCreatedBy(0);
        ffaUser.setUpdatedBy(0);
        ffaUser.setId(model.getId());

        return ffaUser;
    }

    @Override
    public FfaUserDto ConvertEntityToModel(FfaUser entity) {
        return FfaUserDto.builder()
                .name(entity.getName())
                .password(entity.getPassword())
                .roles(entity.getRoles())
                .id(entity.getId())
                .build();
    }

    @Override
    public List<FfaUser> convertModelToEntity(List<FfaUserDto> modelList, AuthUser user) {
        return null;
    }

    @Override
    public List<FfaUser> convertModelToEntity(List<FfaUserDto> modelList) {
        List<FfaUser> ffaUsers = new ArrayList<>();
        for(FfaUserDto model: modelList)
            ffaUsers.add(convertModelToEntity(model));
        return ffaUsers;
    }

    @Override
    public List<FfaUserDto> ConvertEntityToModel(List<FfaUser> entityList) {
        List<FfaUserDto> ffaUserDtos = new ArrayList<>();
        for(FfaUser ffaUser: entityList)
            ffaUserDtos.add(ConvertEntityToModel(ffaUser));
        return ffaUserDtos;
    }

    @Override
    public void applyChanges(FfaUser entity, FfaUserDto model, AuthUser user) {

    }
}
