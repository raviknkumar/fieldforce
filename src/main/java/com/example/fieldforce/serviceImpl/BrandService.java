package com.example.fieldforce.serviceImpl;

import com.example.fieldforce.converter.BrandConverter;
import com.example.fieldforce.entity.Brand;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.BrandDto;
import com.example.fieldforce.repositories.BrandRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class BrandService {

    @Autowired private BrandConverter brandConverter;
    @Autowired private BrandRepo brandRepo;

    public BrandDto addBrand(BrandDto brandDto, AuthUser authUser) {
        Brand brand = brandConverter.convertModelToEntity(brandDto, authUser);
        brand = brandRepo.save(brand);
        return brandConverter.convertEntityToModel(brand);
    }

    public Collection<BrandDto> listAll() {
        List<Brand> brands = brandRepo.findAll();
        return brandConverter.convertEntityToModel(brands);
    }
}
