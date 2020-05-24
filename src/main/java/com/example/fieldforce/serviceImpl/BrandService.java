package com.example.fieldforce.serviceImpl;

import com.example.fieldforce.converter.BrandConverter;
import com.example.fieldforce.entity.Brand;
import com.example.fieldforce.exception.FfaException;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.BrandDto;
import com.example.fieldforce.repositories.BrandRepo;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class BrandService {

    @Autowired private BrandConverter brandConverter;
    @Autowired private BrandRepo brandRepo;

    public BrandDto addBrand(BrandDto brandDto, AuthUser authUser) {
        try {
            // add brand
            Brand brand = brandConverter.convertModelToEntity(brandDto, authUser);
            brand = brandRepo.save(brand);
            return brandConverter.convertEntityToModel(brand);
        }
        catch (DataIntegrityViolationException e){
            for (Throwable t = e.getCause(); t != null; t = t.getCause()) {

                if (PSQLException.class.equals(t.getClass())) {
                    PSQLException postgresException = (PSQLException) t;
                    // In Postgres SQLState 23505=unique_violation
                    if ("23505".equals(postgresException.getSQLState())) {
                        throw new FfaException("Unique constraint violation", "Brand Already exists!");
                    }
                }
            }
        }
        return null;
    }

    public Collection<BrandDto> listAll() {
        List<Brand> brands = brandRepo.findAll();
        return brandConverter.convertEntityToModel(brands);
    }
}
