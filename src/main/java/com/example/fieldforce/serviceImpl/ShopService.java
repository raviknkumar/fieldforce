package com.example.fieldforce.serviceImpl;

import com.example.fieldforce.converter.ShopConverter;
import com.example.fieldforce.entity.Shop;
import com.example.fieldforce.exception.FfaException;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.ShopDto;
import com.example.fieldforce.repositories.ShopRepo;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    @Autowired private ShopRepo shopRepo;
    @Autowired private ShopConverter shopConverter;

    public ShopDto addShop(ShopDto shopDto, AuthUser user){
        try {
            Shop shop = shopConverter.convertModelToEntity(shopDto, user);
            return shopConverter.convertEntityToModel(shopRepo.save(shop));
        }
        catch (DataIntegrityViolationException e){
            for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
                if (PSQLException.class.equals(t.getClass())) {
                    PSQLException postgresException = (PSQLException) t;
                    if ("23505".equals(postgresException.getSQLState())) {
                        throw new FfaException("Unique constraint violation", "Shop with given name Already exists!");
                    }
                }
            }
        }
        return null;
    }

    public List<ShopDto> getShops(){
        List<Shop> shops = shopRepo.findAll();
        return shopConverter.convertEntityToModel(shops);
    }

    public List<ShopDto> getShops(String streetName){
        List<Shop> shops = shopRepo.getAllByStreet(streetName);
        return shopConverter.convertEntityToModel(shops);
    }

    public List<ShopDto> getShopsByIds(List<Integer> shopIds){
        List<Shop> shops = shopRepo.getAllByIdIn(shopIds);
        return shopConverter.convertEntityToModel(shops);
    }


}
