package com.example.fieldforce.converter;

import com.example.fieldforce.entity.Item;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.ItemDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemConverter implements Converter<Item, ItemDto> {
    @Override
    public Item convertModelToEntity(ItemDto model, AuthUser user) {
        Item item = Item.builder()
                .name(model.getName())
                .brandId(model.getBrandId())
                .price(model.getPrice())
                .inventory(model.getInventory())
                .build();

        item.setId(model.getId());
        item.setCreatedBy(user.getId());
        item.setUpdatedBy(user.getId());

        return item;
    }

    @Override
    public Item convertModelToEntity(ItemDto model) {
        Item item = Item.builder()
                .name(model.getName())
                .brandId(model.getBrandId())
                .price(model.getPrice())
                .inventory(model.getInventory())
                .build();

        item.setId(model.getId());
        item.setCreatedBy(0);
        item.setUpdatedBy(0);

        return item;
    }

    @Override
    public ItemDto convertEntityToModel(Item entity) {
        return ItemDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .brandId(entity.getBrandId())
                .price(entity.getPrice())
                .inventory(entity.getInventory())
                .build();

    }

    @Override
    public List<Item> convertModelToEntity(List<ItemDto> modelList, AuthUser user) {
        List<Item> items = new ArrayList<>();
        for(ItemDto itemDto: modelList)
            items.add(convertModelToEntity(itemDto, user));
        return items;
    }

    @Override
    public List<Item> convertModelToEntity(List<ItemDto> modelList) {
        return null;
    }

    @Override
    public List<ItemDto> convertEntityToModel(List<Item> entityList) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for(Item item: entityList)
            itemDtos.add(convertEntityToModel(item));
        return itemDtos;
    }

    @Override
    public void applyChanges(Item entity, ItemDto model, AuthUser user) {

    }
}
