package com.example.fieldforce.serviceImpl;

import com.example.fieldforce.converter.SaleOrderConverter;
import com.example.fieldforce.converter.SaleOrderDetailConverter;
import com.example.fieldforce.entity.SaleOrder;
import com.example.fieldforce.entity.SaleOrderDetail;
import com.example.fieldforce.exception.FfaException;
import com.example.fieldforce.helper.*;
import com.example.fieldforce.model.*;
import com.example.fieldforce.repositories.SaleOrderDetailRepo;
import com.example.fieldforce.repositories.SaleOrderRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SaleOrderService {

    static String SODheaders[] = new String[]{"Item Name", "Pieces", "Boxes", "Price", "Tax", "Amount"};

    @Autowired private SaleOrderRepo saleOrderRepo;
    @Autowired private SaleOrderConverter saleOrderConverter;
    @Autowired private SaleOrderDetailConverter saleOrderDetailConverter;
    @Autowired private SaleOrderDetailRepo saleOrderDetailRepo;
    @Autowired private PdfUtils pdfUtils;
    @Autowired private ShopService shopService;
    @Autowired private ItemService itemService;

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
        List<Integer> itemIds = saleOrderDetails.stream().map(SaleOrderDetail::getItemId).collect(Collectors.toList());
        List<ItemDto> itemDtos = itemService.getAllByIds(itemIds);
        Map<Integer, ItemDto> itemIdToItemMap = itemDtos.stream().collect(Collectors.toMap(ItemDto::getId, Function.identity()));
        SaleOrderDto saleOrderDto = saleOrderConverter.convertEntityToModel(saleOrder);
        List<SaleOrderDetailDto> saleOrderDetailDtos = saleOrderDetailConverter.convertEntityToModel(saleOrderDetails);
        Integer itemId = 0;
        for(SaleOrderDetailDto saleOrderDetailDto : saleOrderDetailDtos){
            itemId = saleOrderDetailDto.getItemId();
            saleOrderDetailDto.setTaxPrice(itemIdToItemMap.get(itemId).getTaxPercent());
        }
        return SaleOrderRequestDto.convertSOAndSOD(saleOrderDto, saleOrderDetailDtos);
    }

    public List<ShopDto> fetchSaleOrderByDate(String orderDate) {
        List<SaleOrder> saleOrders = saleOrderRepo.findAllByOrderDate(orderDate);
        List<ShopDto> shopDtos = new ArrayList<>();

        List<ShopDto> shopsByIds = shopService.getShopsByIds(saleOrders.stream().map(SaleOrder::getShopId).collect(Collectors.toList()));
        Map<Integer, ShopDto> shopIdToShopDtoMap = shopsByIds.stream().collect(Collectors.toMap(ShopDto::getId, Function.identity()));

        if(!CollectionUtils.isEmpty(saleOrders)){
            for(SaleOrder saleOrder : saleOrders){
                shopDtos.add(ShopDto.builder()
                .name(saleOrder.getShopName())
                .street(shopIdToShopDtoMap.get(saleOrder.getShopId()).getStreet())
                .id(saleOrder.getShopId())
                .build());
            }
            return shopDtos;
        }
        return null;
    }

    public boolean createOutputFile(String orderDate, Integer shopId, String type) throws Exception {
        Optional<SaleOrder> saleOrderOptional = saleOrderRepo.findByShopIdAndOrderDate(shopId, orderDate);
        if(!saleOrderOptional.isPresent()){
            throw new FfaException("No Order Present", "No Order found for given filter");
        }
        SaleOrder saleOrder = saleOrderOptional.get();
        List<SaleOrderDetail> saleOrderDetails = saleOrderDetailRepo.findAllBySaleOrderId(saleOrder.getId());

        List<Integer> itemIds = saleOrderDetails.stream().map(SaleOrderDetail::getItemId).collect(Collectors.toList());
        List<ItemDto> itemDtos = itemService.getAllByIds(itemIds);
        Map<Integer, ItemDto> itemIdToItemMap = itemDtos.stream().collect(Collectors.toMap(ItemDto::getId, Function.identity()));

        if(type.equals("excel")) {
            createExcelFile(saleOrder.getShopName(), orderDate, saleOrderDetails, itemIdToItemMap);
            return true;
        }
        else {
             createPdfFile(saleOrder.getShopName(), orderDate, saleOrderDetails, itemIdToItemMap);
             return true;
        }
    }

    private void createPdfFile(String shopName, String orderDate, List<SaleOrderDetail> saleOrderDetails,
                               Map<Integer, ItemDto> itemIdToItemMap) {
        String filePath = FileHelper.getFilePath(shopName, orderDate, "pdf");
        pdfUtils.createPDF(filePath, shopName, orderDate, saleOrderDetails, itemIdToItemMap);

    }

    private XSSFWorkbook createExcelFile(String shopName, String orderDate, List<SaleOrderDetail> saleOrderDetails
            , Map<Integer, ItemDto> itemIdToItemMap) throws Exception {
        String headers1[] = new String[]{shopName, orderDate};

        XSSFWorkbook xssfWorkbook = ExcelUtils.getNewXSSFWorkbook();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("Order_Sheet");
        String filePath = FileHelper.getFilePath(shopName, orderDate, "excel");

        ExcelUtils.writeHeaderToSheet(xssfSheet, headers1);

        List<String[]> dataList = new ArrayList<>();
        for (SaleOrderDetail saleOrderDetail : saleOrderDetails) {
            ItemDto itemDto = itemIdToItemMap.get(saleOrderDetail.getItemId());
            saleOrderDetail.setOriginalPrice(itemDto.getBoxPrice());
            saleOrderDetail.setTaxPrice(itemDto.getTaxPercent());
            dataList.add(getSODDataAsStringArray(saleOrderDetail));
        }
        ExcelUtils.writeDataToSheet(xssfSheet, SODheaders, dataList);
        FileUtils.saveFile(filePath, xssfWorkbook);
        log.info("Saved Workbook successfully");
        return xssfWorkbook;
    }

    private String[] getSODDataAsStringArray(SaleOrderDetail saleOrderDetail){
        return new String[]{
                StringUtils.getCleanString(saleOrderDetail.getItemName()),
                StringUtils.getCleanString(saleOrderDetail.getPieces()),
                StringUtils.getCleanString(saleOrderDetail.getBoxes()),
                StringUtils.getCleanString(saleOrderDetail.getOriginalPrice()),
                StringUtils.getCleanString(saleOrderDetail.getTaxPrice()),
                StringUtils.getCleanString(saleOrderDetail.getSalePrice()),
        };
    }

    @Transactional
    public void deleteData() {
        LocalDate today = LocalDate.now();
        LocalDate prevDate = DateUtils.addDaysToLocalDate(today, -15);
        log.info("{}", prevDate.toString());
        List<SaleOrder> saleOrders =  saleOrderRepo.deleteAllByOrderDate(prevDate.toString());
        List<Integer> soIds = saleOrders.stream().map(SaleOrder::getId).collect(Collectors.toList());
        log.info("{}", soIds);
        saleOrderDetailRepo.deleteAllBySaleOrderIdIn(soIds);
        log.info("Data deleted successfully");
    }

    public String savePrice(SaleOrderRequestDto saleOrderRequestDto, AuthUser authUser) {
        List<SaleOrderDetailDto> saleOrderDetailDtos = saleOrderRequestDto.getSaleOrderDetails();
        List<SaleOrderDetail> saleOrderDetails = saleOrderDetailConverter.convertModelToEntity(saleOrderDetailDtos, authUser);

        Double totalOriginalPrice = 0d, totalSalePrice =0d;
        if(!CollectionUtils.isEmpty(saleOrderDetails)){
            for(SaleOrderDetail saleOrderDetail : saleOrderDetails){
                Double originalPrice = saleOrderDetail.getOriginalPrice();
                totalOriginalPrice += originalPrice;
                Double salePrice = originalPrice + (originalPrice * saleOrderDetail.getTaxPrice() / 100.0);
                saleOrderDetail.setSalePrice(salePrice);
                totalSalePrice += salePrice;
            }
        }
        saleOrderDetailRepo.saveAll(saleOrderDetails);

        SaleOrderDto saleOrderDto = saleOrderRequestDto.getSaleOrder();
        saleOrderDto.setTotalPrice(totalSalePrice);
        saleOrderDto.setTotalTax(totalSalePrice - totalOriginalPrice);

        saleOrderRepo.save(saleOrderConverter.convertModelToEntity(saleOrderDto, authUser));
        return "success";
    }
}