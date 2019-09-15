package com.example.fieldforce.serviceImpl;

import com.example.fieldforce.converter.StreetConverter;
import com.example.fieldforce.entity.Street;
import com.example.fieldforce.exception.FfaException;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.StreetDto;
import com.example.fieldforce.repositories.StreetRepo;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreetService {

    @Autowired private StreetConverter streetConverter;
    @Autowired private StreetRepo streetRepo;

    public StreetDto createStreet(StreetDto streetDto, AuthUser authuser) {
        try {
            Street street = streetConverter.convertModelToEntity(streetDto, authuser);
            return streetConverter.convertEntityToModel(streetRepo.save(street));
        }
        catch (DataIntegrityViolationException e){
            for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
                if (PSQLException.class.equals(t.getClass())) {
                    PSQLException postgresException = (PSQLException) t;
                    if ("23505".equals(postgresException.getSQLState())) {
                        throw new FfaException("Unique constraint violation", "Beat with given name Already exists!");
                    }
                }
            }
        }
        return null;
    }

    public List<StreetDto> listAll() {
        List<Street> streets = streetRepo.findAll();
        return streetConverter.convertEntityToModel(streets);
    }
}
