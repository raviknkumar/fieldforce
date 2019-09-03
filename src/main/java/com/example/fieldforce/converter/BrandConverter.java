package com.example.fieldforce.converter;

import com.example.fieldforce.entity.Brand;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.BrandDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandConverter implements Converter<Brand, BrandDto> {
    @Override
    public Brand convertModelToEntity(BrandDto model, AuthUser user) {
        Brand brand = Brand.builder()
                .name(model.getName())
                .build();
        brand.setId(model.getId());
        brand.setCreatedBy(user.getId());
        brand.setUpdatedBy(user.getId());

        return brand;
    }

    @Override
    public Brand convertModelToEntity(BrandDto model) {
        Brand brand = Brand.builder()
                .name(model.getName())
                .build();
        brand.setId(model.getId());
        brand.setCreatedBy(0);
        brand.setUpdatedBy(0);

        return brand;
    }

    @Override
    public BrandDto convertEntityToModel(Brand entity) {
        return BrandDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    public List<Brand> convertModelToEntity(List<BrandDto> modelList, AuthUser user) {
        List<Brand> brands = new ArrayList<>();
        for(BrandDto model : modelList){
            brands.add(convertModelToEntity(model, user));
        }
        return brands;
    }

    @Override
    public List<Brand> convertModelToEntity(List<BrandDto> modelList) {
        return null;
    }

    @Override
    public List<BrandDto> convertEntityToModel(List<Brand> entityList) {
        List<BrandDto> brandDtos = new ArrayList<>();
        for(Brand entity : entityList){
            brandDtos.add(convertEntityToModel(entity));
        }
        return brandDtos;
    }

    @Override
    public void applyChanges(Brand entity, BrandDto model, AuthUser user) {

    }
}
