package com.example.fieldforce.converter;

import com.example.fieldforce.entity.SaleOrder;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.SaleOrderDto;
import com.example.fieldforce.model.SaleOrderRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleOrderConverter implements Converter<SaleOrder, SaleOrderDto> {
    @Override
    public SaleOrder convertModelToEntity(SaleOrderDto model, AuthUser user) {
        SaleOrder saleOrder = SaleOrder.builder()
                .shopName(model.getShopName())
                .shopId(model.getShopId())
                .orderDate(model.getOrderDate())
                .totalPrice(model.getTotalPrice())
                .totalTax(model.getTotalPrice())
                .delieveryCharge(model.getDelieveryCharge())
                .build();

        saleOrder.setId(model.getId());
        saleOrder.setCreatedBy(user.getId());
        saleOrder.setUpdatedBy(user.getId());

        return saleOrder;

    }

    @Override
    public SaleOrder convertModelToEntity(SaleOrderDto model) {
        return null;
    }

    @Override
    public SaleOrderDto convertEntityToModel(SaleOrder entity) {
        return SaleOrderDto.builder()
                .id(entity.getId())
                .shopName(entity.getShopName())
                .shopId(entity.getShopId())
                .orderDate(entity.getOrderDate())
                .totalPrice(entity.getTotalPrice())
                .totalTax(entity.getTotalPrice())
                .delieveryCharge(entity.getDelieveryCharge())
                .build();
    }

    @Override
    public List<SaleOrder> convertModelToEntity(List<SaleOrderDto> modelList, AuthUser user) {
        List<SaleOrder> saleOrders = new ArrayList<>();
        for (SaleOrderDto saleOrderDto : modelList)
            saleOrders.add(convertModelToEntity(saleOrderDto, user));
        return saleOrders;
    }

    @Override
    public List<SaleOrder> convertModelToEntity(List<SaleOrderDto> modelList) {
        List<SaleOrder> saleOrders = new ArrayList<>();
        for (SaleOrderDto saleOrderDto : modelList)
            saleOrders.add(convertModelToEntity(saleOrderDto));
        return saleOrders;
    }

    @Override
    public List<SaleOrderDto> convertEntityToModel(List<SaleOrder> entityList) {
        List<SaleOrderDto> saleOrderDtos = new ArrayList<>();
        for (SaleOrder saleOrder : entityList)
            saleOrderDtos.add(convertEntityToModel(saleOrder));
        return saleOrderDtos;
    }

    @Override
    public void applyChanges(SaleOrder entity, SaleOrderDto model, AuthUser user) {

        entity.setId(model.getId());
        entity.setShopName(model.getShopName());
        entity.setShopId(model.getShopId());
        entity.setOrderDate(model.getOrderDate());
        entity.setTotalPrice(model.getTotalPrice());
        entity.setTotalTax(model.getTotalTax());
        entity.setDelieveryCharge(model.getDelieveryCharge());
        entity.setUpdatedBy(user.getId());
    }
}
