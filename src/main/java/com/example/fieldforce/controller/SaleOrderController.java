package com.example.fieldforce.controller;

import com.example.fieldforce.exception.FfaException;
import com.example.fieldforce.helper.AuthHelper;
import com.example.fieldforce.helper.FileHelper;
import com.example.fieldforce.helper.FileStorageService;
import com.example.fieldforce.helper.FileUtils;
import com.example.fieldforce.model.*;
import com.example.fieldforce.serviceImpl.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/so")
public class SaleOrderController {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private FileStorageService fileStorageService;

    public final String CONTENT_TYPE_OCTET = "application/octet-stream";

    @PostMapping
    public ApiResponse<SaleOrderRequestDto> createOrUpdateSO(@RequestBody SaleOrderRequestDto saleOrderRequest) {
        AuthUser user = AuthHelper.getAuthUser(httpServletRequest.getHeader(Constant.AUTH_CONSTANT));
        if (saleOrderRequest.getSaleOrder().getId() == null) {
            saleOrderRequest = saleOrderService.createSaleOrder(saleOrderRequest, user);
        } else
            saleOrderRequest = saleOrderService.editSaleOrder(saleOrderRequest, user);

        return new ApiResponse<>(saleOrderRequest);
    }

    @GetMapping
    public ApiResponse<SaleOrderRequestDto> fetchByShopIdAndDate(@RequestParam("shopId") Integer shopId, @RequestParam("orderDate") String orderDate) {
        SaleOrderRequestDto saleOrderRequestDto = saleOrderService.fetchSaleOrder(shopId, orderDate);
        return new ApiResponse<>(saleOrderRequestDto);
    }

    @GetMapping("/date")
    public ApiResponse<List<ShopDto>> fetchByDate(@RequestParam("orderDate") String orderDate) {
        List<ShopDto> shopDtos = saleOrderService.fetchSaleOrderByDate(orderDate);
        if (shopDtos == null) {
            throw new FfaException("NO Orders Found", "There are no customer orders for given date");
        }
        return new ApiResponse<>(shopDtos);
    }

    @GetMapping("/generate")
    public ResponseEntity<byte[]> create(@RequestParam("orderDate") String orderDate,
                                         @RequestParam("shopId") Integer shopId,
                                         @RequestParam("shopName") String shopName,
                                         @RequestParam("type") String type) throws Exception {
        Object outputFile = saleOrderService.createOutputFile(orderDate, shopId, type);
        if (outputFile == null)
            throw new FfaException("File Generation Failed", " Failed to create " + type);

        String filePath = FileHelper.getFilePath(shopName, orderDate, type);

        byte[] resource = fileStorageService.loadFileAsBytes(filePath);
        String contentType = CONTENT_TYPE_OCTET;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "\"")
                .body(resource);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel(@RequestParam("orderDate") String orderDate,
                                                @RequestParam("shopId") Integer shopId,
                                                @RequestParam("shopName") String shopName,
                                                @RequestParam("type") String type) throws Exception {
        String filePath = FileHelper.getFilePath(shopName, orderDate, type);
        if (!FileUtils.isFileExists(filePath))
            return null;

        byte[] resource = fileStorageService.loadFileAsBytes(filePath);
        String contentType = CONTENT_TYPE_OCTET;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "\"")
                .body(resource);
    }


}
