package com.example.fieldforce.converter;

import com.example.fieldforce.entity.Shop;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.ShopDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShopConverter implements Converter<Shop, ShopDto> {

    @Override
    public Shop convertModelToEntity(ShopDto model, AuthUser user) {
        Shop shop = Shop.builder()
                .name(model.getName())
                .street(model.getStreet())
                .type1(model.getType1())
                .type2(model.getType2())
                .addressLine1(model.getAddressLine1())
                .addressLine2(model.getAddressLine2())
                .phoneNumber(model.getPhoneNumber())
                .build();

        shop.setId(model.getId());
        shop.setCreatedBy(model.getId());
        shop.setUpdatedBy(model.getId());

        return shop;
    }

    @Override
    public Shop convertModelToEntity(ShopDto model) {
        Shop shop = Shop.builder()
                .name(model.getName())
                .street(model.getStreet())
                .type1(model.getType1())
                .type2(model.getType2())
                .addressLine1(model.getAddressLine1())
                .addressLine2(model.getAddressLine2())
                .phoneNumber(model.getPhoneNumber())
                .build();

        shop.setId(model.getId());
        shop.setCreatedBy(0);
        shop.setUpdatedBy(0);

        return shop;
    }

    @Override
    public ShopDto ConvertEntityToModel(Shop entity) {
        return ShopDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .street(entity.getStreet())
                .type1(entity.getType1())
                .type2(entity.getType2())
                .addressLine1(entity.getAddressLine1())
                .addressLine2(entity.getAddressLine2())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }

    @Override
    public List<Shop> convertModelToEntity(List<ShopDto> modelList, AuthUser user) {
        List<Shop> shops = new ArrayList<>();
        for (ShopDto shopDto : modelList)
            shops.add(convertModelToEntity(shopDto, user));
        return shops;
    }

    @Override
    public List<Shop> convertModelToEntity(List<ShopDto> modelList) {
        List<Shop> shops = new ArrayList<>();
        for (ShopDto shopDto : modelList)
            shops.add(convertModelToEntity(shopDto));
        return shops;
    }

    @Override
    public List<ShopDto> ConvertEntityToModel(List<Shop> entityList) {
        List<ShopDto> shopDtos = new ArrayList<>();
        for (Shop shop : entityList)
            shopDtos.add(ConvertEntityToModel(shop));
        return shopDtos;
    }

    @Override
    public void applyChanges(Shop entity, ShopDto model, AuthUser user) {
        entity.setName(model.getName());
        entity.setStreet(model.getStreet());
        entity.setType1(model.getType1());
        entity.setType2(model.getType2());
        entity.setAddressLine1(model.getAddressLine1());
        entity.setAddressLine2(model.getAddressLine2());
        entity.setPhoneNumber(model.getPhoneNumber());
        entity.setId(model.getId());

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedAt(new Date());
    }
}
