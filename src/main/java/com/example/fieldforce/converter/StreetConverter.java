package com.example.fieldforce.converter;

import com.example.fieldforce.entity.Brand;
import com.example.fieldforce.entity.Street;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.BrandDto;
import com.example.fieldforce.model.StreetDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StreetConverter implements Converter<Street, StreetDto> {
    @Override
    public Street convertModelToEntity(StreetDto model, AuthUser user) {
        Street street = Street.builder()
                .name(model.getName())
                .build();
        street.setId(model.getId());
        street.setCreatedBy(user.getId());
        street.setUpdatedBy(user.getId());

        return street;
    }

    @Override
    public Street convertModelToEntity(StreetDto model) {
        Street street = Street.builder()
                .name(model.getName())
                .build();
        street.setId(model.getId());
        street.setCreatedBy(0);
        street.setUpdatedBy(0);

        return street;
    }

    @Override
    public StreetDto convertEntityToModel(Street entity) {
        return StreetDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    public List<Street> convertModelToEntity(List<StreetDto> modelList, AuthUser user) {
        List<Street> streets = new ArrayList<>();
        for(StreetDto model : modelList){
            streets.add(convertModelToEntity(model, user));
        }
        return streets;
    }

    @Override
    public List<Street> convertModelToEntity(List<StreetDto> modelList) {
        return null;
    }

    @Override
    public List<StreetDto> convertEntityToModel(List<Street> entityList) {
        List<StreetDto> streetDtos = new ArrayList<>();
        for(Street entity : entityList){
            streetDtos.add(convertEntityToModel(entity));
        }
        return streetDtos;
    }

    @Override
    public void applyChanges(Street entity, StreetDto model, AuthUser user) {

    }
}
