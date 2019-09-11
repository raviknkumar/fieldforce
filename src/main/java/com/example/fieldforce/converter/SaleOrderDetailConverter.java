package com.example.fieldforce.converter;

import com.example.fieldforce.entity.SaleOrderDetail;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.SaleOrderDetailDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleOrderDetailConverter implements Converter<SaleOrderDetail, SaleOrderDetailDto> {
    @Override
    public SaleOrderDetail convertModelToEntity(SaleOrderDetailDto model, AuthUser user) {
        SaleOrderDetail saleOrderDetail = SaleOrderDetail.builder()
       .saleOrderId(model.getSaleOrderId())
        .itemId(model.getItemId())
        .itemName(model.getItemName())        
        .salePrice(model.getSalePrice())
        .taxPrice(model.getTaxPrice())
        .originalPrice(model.getPurchasePrice())
        .pieces(model.getPieces())
        .boxes(model.getBoxes())
        .discount(model.getDiscount())
                .build();
        
        saleOrderDetail.setId(model.getId());
        saleOrderDetail.setCreatedBy(user.getId());
        saleOrderDetail.setUpdatedBy(user.getId());

        return saleOrderDetail;
    }

    @Override
    public SaleOrderDetail convertModelToEntity(SaleOrderDetailDto model) {
        return null;
    }

    @Override
    public SaleOrderDetailDto convertEntityToModel(SaleOrderDetail entity) {
        return SaleOrderDetailDto.builder()
                .id(entity.getId())
                .saleOrderId(entity.getSaleOrderId())
                .itemId(entity.getItemId())
                .itemName(entity.getItemName())
                .salePrice(entity.getSalePrice())
                .taxPrice(entity.getTaxPrice())
                .purchasePrice(entity.getOriginalPrice())
                .pieces(entity.getPieces())
                .boxes(entity.getBoxes())
                .discount(entity.getDiscount())
                .build();

    }

    @Override
    public List<SaleOrderDetail> convertModelToEntity(List<SaleOrderDetailDto> modelList, AuthUser user) {
        List<SaleOrderDetail> saleOrderDetails = new ArrayList<>();
        for(SaleOrderDetailDto saleOrderDetailDto : modelList)
            saleOrderDetails.add(convertModelToEntity(saleOrderDetailDto, user));
        return saleOrderDetails;
    }

    @Override
    public List<SaleOrderDetail> convertModelToEntity(List<SaleOrderDetailDto> modelList) {
        return null;
    }

    @Override
    public List<SaleOrderDetailDto> convertEntityToModel(List<SaleOrderDetail> entityList) {
        List<SaleOrderDetailDto> saleOrderDetailDtos = new ArrayList<>();
        for(SaleOrderDetail saleOrderDetail : entityList)
            saleOrderDetailDtos.add(convertEntityToModel(saleOrderDetail));
        return saleOrderDetailDtos;
    }

    @Override
    public void applyChanges(SaleOrderDetail entity, SaleOrderDetailDto model, AuthUser user) {
         entity.setId(model.getId());
         entity.setSaleOrderId(model.getSaleOrderId());
         entity.setItemId(model.getItemId());
         entity.setItemName(model.getItemName());
         entity.setSalePrice(model.getSalePrice());
         entity.setTaxPrice(model.getTaxPrice());
         entity.setOriginalPrice(model.getPurchasePrice());
         entity.setPieces(model.getPieces());
         entity.setBoxes(model.getBoxes());
         entity.setDiscount(model.getDiscount());
         entity.setUpdatedBy(user.getId());
    }
}
