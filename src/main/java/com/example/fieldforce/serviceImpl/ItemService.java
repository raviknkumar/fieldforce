package com.example.fieldforce.serviceImpl;

import com.example.fieldforce.converter.ItemConverter;
import com.example.fieldforce.entity.Item;
import com.example.fieldforce.exception.FfaException;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.ItemDto;
import com.example.fieldforce.repositories.ItemRepo;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired private ItemRepo itemRepo;
    @Autowired private ItemConverter itemConverter;

    public ItemDto addItem(ItemDto itemDto, AuthUser user){
        try {
            Item item = itemRepo.save(itemConverter.convertModelToEntity(itemDto, user));
            return itemConverter.convertEntityToModel(item);
        }
        catch (DataIntegrityViolationException e){
            for (Throwable t = e.getCause(); t != null; t = t.getCause()) {

                if (PSQLException.class.equals(t.getClass())) {
                    PSQLException postgresException = (PSQLException) t;
                    if ("23505".equals(postgresException.getSQLState())) {
                        throw new FfaException("Unique constraint violation", "Item with given name already exists!");
                    }
                }
            }
        }
        return null;
    }

    public List<ItemDto> getAll(){
        List<Item> items = itemRepo.findAll();
        return itemConverter.convertEntityToModel(items);
    }

    public List<ItemDto> getAllByIds(List<Integer> ids){
        List<Item> items = itemRepo.findAllByIdIn(ids);
        return itemConverter.convertEntityToModel(items);
    }
}
