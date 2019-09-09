package com.example.fieldforce.controller;

import com.example.fieldforce.exception.FfaException;
import com.example.fieldforce.helper.AuthHelper;
import com.example.fieldforce.model.*;
import com.example.fieldforce.serviceImpl.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/so")
public class SaleOrderController {

    @Autowired private HttpServletRequest httpServletRequest;
    @Autowired private SaleOrderService saleOrderService;

    @PostMapping
    public ApiResponse<SaleOrderRequestDto> createOrUpdateSO(@RequestBody SaleOrderRequestDto saleOrderRequest){
        AuthUser user = AuthHelper.getAuthUser(httpServletRequest.getHeader(Constant.AUTH_CONSTANT));
        if(saleOrderRequest.getSaleOrder().getId() == null){
            saleOrderRequest = saleOrderService.createSaleOrder(saleOrderRequest, user);
        }
        else
            saleOrderRequest =saleOrderService.editSaleOrder(saleOrderRequest, user);

        return new ApiResponse<>(saleOrderRequest);
    }

    @GetMapping
    public ApiResponse<SaleOrderRequestDto> fetchByShopIdAndDate(@RequestParam("shopId") Integer shopId, @RequestParam("orderDate") String orderDate){
        SaleOrderRequestDto saleOrderRequestDto = saleOrderService.fetchSaleOrder(shopId, orderDate);
        return new ApiResponse<>(saleOrderRequestDto);
    }

    @GetMapping("/date")
    public ApiResponse<List<ShopDto>> fetchByDate(@RequestParam("date") String orderDate){
        List<ShopDto> shopDtos = saleOrderService.fetchSaleOrderByDate(orderDate);
        if(shopDtos == null){
            throw new FfaException("NO Orders Found","There are no customer orders for given date");
        }
        return new ApiResponse<>(shopDtos);
    }
}
