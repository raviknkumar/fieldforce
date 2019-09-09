package com.example.fieldforce.serviceImpl;

import com.example.fieldforce.converter.SaleOrderConverter;
import com.example.fieldforce.converter.SaleOrderDetailConverter;
import com.example.fieldforce.entity.SaleOrder;
import com.example.fieldforce.entity.SaleOrderDetail;
import com.example.fieldforce.exception.FfaException;
import com.example.fieldforce.helper.ExcelUtils;
import com.example.fieldforce.helper.FileHelper;
import com.example.fieldforce.helper.FileUtils;
import com.example.fieldforce.helper.StringUtils;
import com.example.fieldforce.model.*;
import com.example.fieldforce.repositories.SaleOrderDetailRepo;
import com.example.fieldforce.repositories.SaleOrderRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
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

    public List<ShopDto> fetchSaleOrderByDate(String orderDate) {
        List<SaleOrder> saleOrders = saleOrderRepo.findAllByOrderDate(orderDate);
        List<ShopDto> shopDtos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(saleOrders)){
            for(SaleOrder saleOrder : saleOrders){
                shopDtos.add(ShopDto.builder()
                .name(saleOrder.getShopName())
                .id(saleOrder.getShopId())
                .build());
            }
            return shopDtos;
        }
        return null;
    }

    public XSSFWorkbook createExcelSheet(String orderDate, Integer shopId) throws Exception {
        Optional<SaleOrder> saleOrderOptional = saleOrderRepo.findByShopIdAndOrderDate(shopId, orderDate);
        if(!saleOrderOptional.isPresent()){
            throw new FfaException("No Order Present", "No Order found for given filter");
        }
        SaleOrder saleOrder = saleOrderOptional.get();
        List<SaleOrderDetail> saleOrderDetails = saleOrderDetailRepo.findAllBySaleOrderId(saleOrder.getId());

        String headers1[] = new String[]{saleOrder.getShopName(), saleOrder.getOrderDate() };
        String SODheaders[] = new String[]{"Item Name", "Pieces", "Boxes", "Sale Price"};
        XSSFWorkbook xssfWorkbook = ExcelUtils.getNewXSSFWorkbook();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("Order_Sheet");
        String filePath = FileHelper.getFilePath(saleOrder.getShopName(), orderDate);

        ExcelUtils.writeHeaderToSheet(xssfSheet, headers1);

        List<String[]> dataList = new ArrayList<>();
        for(SaleOrderDetail saleOrderDetail : saleOrderDetails){
            dataList.add(new String[]{
                   StringUtils.getCleanString(saleOrderDetail.getItemName()),
                    StringUtils.getCleanString(saleOrderDetail.getPieces()),
                    StringUtils.getCleanString(saleOrderDetail.getBoxes()),
                    StringUtils.getCleanString(saleOrderDetail.getSalePrice()),
            });
        }
        ExcelUtils.writeDataToSheet(xssfSheet, SODheaders, dataList);
        FileUtils.saveFile(filePath, xssfWorkbook);
        log.info("Saved Workbook successfully");
        return xssfWorkbook;
    }
}