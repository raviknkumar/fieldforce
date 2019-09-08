package com.example.fieldforce.serviceImpl;

import com.example.fieldforce.converter.SaleOrderConverter;
import com.example.fieldforce.converter.SaleOrderDetailConverter;
import com.example.fieldforce.entity.SaleOrder;
import com.example.fieldforce.entity.SaleOrderDetail;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.SaleOrderDetailDto;
import com.example.fieldforce.model.SaleOrderDto;
import com.example.fieldforce.model.SaleOrderRequestDto;
import com.example.fieldforce.repositories.SaleOrderDetailRepo;
import com.example.fieldforce.repositories.SaleOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SaleOrderService {

    @Autowired private SaleOrderRepo saleOrderRepo;
    @Autowired private SaleOrderConverter saleOrderConverter;
    @Autowired private SaleOrderDetailConverter saleOrderDetailConverter;
    @Autowired private SaleOrderDetailRepo saleOrderDetailRepo;

    public SaleOrderRequestDto createSaleOrder(SaleOrderRequestDto saleOrderRequestDto, AuthUser user){
        List<SaleOrderDetailDto> saleOrderDetails = saleOrderRequestDto.getSaleOrderDetails();
        SaleOrderDto saleOrderDto = saleOrderRequestDto.getSaleOrder();

        SaleOrder saleOrder = saleOrderRepo.save(saleOrderConverter.convertModelToEntity(saleOrderDto, user));
        if(!CollectionUtils.isEmpty(saleOrderDetails)) {
            for (SaleOrderDetailDto saleOrderDetailDto : saleOrderDetails) {
                saleOrderDetailDto.setSaleOrderId(saleOrder.getId());
            }
            List<SaleOrderDetail> entitySaleOrderDetails = saleOrderDetailConverter.convertModelToEntity(saleOrderDetails, user);
            entitySaleOrderDetails = saleOrderDetailRepo.saveAll(entitySaleOrderDetails);
            saleOrderDetails = saleOrderDetailConverter.convertEntityToModel(entitySaleOrderDetails);
        }
        saleOrderDto = saleOrderConverter.convertEntityToModel(saleOrder);
        return SaleOrderRequestDto.convertSOAndSOD(saleOrderDto, saleOrderDetails);
    }

    public SaleOrderRequestDto editSaleOrder(SaleOrderRequestDto saleOrderRequestDto, AuthUser user){

        Integer saleOrderId = saleOrderRequestDto.getSaleOrder().getId();

        List<SaleOrderDetailDto> sodDtos = saleOrderRequestDto.getSaleOrderDetails();
        List<SaleOrderDetail> existingSODs = saleOrderDetailRepo.findAllBySaleOrderId(saleOrderId);
        Map<Integer, SaleOrderDetail> saleOrderDetaildToSaleOrderDetailMap = existingSODs.stream().collect(Collectors.toMap(SaleOrderDetail::getId, Function.identity()));
        List<SaleOrderDetail> saleOrderDetailsToPersist = new ArrayList<>();

        for(SaleOrderDetailDto saleOrderDetailDto : sodDtos){
            Integer saleOrderDetailId = saleOrderDetailDto.getId();
            if(saleOrderDetailId!=null) {
                SaleOrderDetail saleOrderDetail = saleOrderDetaildToSaleOrderDetailMap.get(saleOrderDetailDto.getId());
                if ((saleOrderDetail.getBoxes() != null && !saleOrderDetail.getBoxes().equals(saleOrderDetailDto.getBoxes()))
                        || (saleOrderDetail.getBoxes() == null && saleOrderDetailDto.getBoxes()!=null)
                        || (saleOrderDetail.getPieces() != null && !saleOrderDetail.getPieces().equals(saleOrderDetailDto.getPieces()))
                        || (saleOrderDetail.getPieces() == null && saleOrderDetailDto.getPieces()!=null)) {
                    saleOrderDetailConverter.applyChanges(saleOrderDetail, saleOrderDetailDto, user);
                    saleOrderDetailsToPersist.add(saleOrderDetail);
                }
            }
            else {
                // new order
                SaleOrderDetail saleOrderDetail = saleOrderDetailConverter.convertModelToEntity(saleOrderDetailDto, user);
                saleOrderDetail.setSaleOrderId(saleOrderId);
                saleOrderDetailsToPersist.add(saleOrderDetail);
            }
        }

        saleOrderDetailsToPersist = saleOrderDetailRepo.saveAll(saleOrderDetailsToPersist);

        saleOrderRequestDto.setSaleOrderDetails(saleOrderDetailConverter.convertEntityToModel(saleOrderDetailsToPersist));
        return saleOrderRequestDto;
    }

    private void calculateCostChange(List<SaleOrderDetail> saleOrderDetailsToPersist, SaleOrderRequestDto saleOrderRequestDto) {

    }


    public SaleOrderRequestDto fetchSaleOrder(Integer shopId, String orderDate) {
        Optional<SaleOrder> saleOrderOptional = saleOrderRepo.findByShopIdAndOrderDate(shopId, orderDate);
        SaleOrder saleOrder = saleOrderOptional.orElse(null);
        if(saleOrder == null){
            return null;
        }
        List<SaleOrderDetail> saleOrderDetails = saleOrderDetailRepo.findAllBySaleOrderId(saleOrder.getId());
        SaleOrderDto saleOrderDto = saleOrderConverter.convertEntityToModel(saleOrder);
        List<SaleOrderDetailDto> saleOrderDetailDtos = saleOrderDetailConverter.convertEntityToModel(saleOrderDetails);
        return SaleOrderRequestDto.convertSOAndSOD(saleOrderDto, saleOrderDetailDtos);
    }
}
