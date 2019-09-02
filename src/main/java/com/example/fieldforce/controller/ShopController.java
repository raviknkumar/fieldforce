package com.example.fieldforce.controller;

import com.example.fieldforce.entity.Shop;
import com.example.fieldforce.model.ApiResponse;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.Constant;
import com.example.fieldforce.model.ShopDto;
import com.example.fieldforce.serviceImpl.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired private HttpServletRequest request;
    @Autowired private ShopService shopService;

    @PostMapping()
    public ApiResponse<ShopDto> addShop(@RequestBody ShopDto shopDto){
        ShopDto shop = shopService.addShop(shopDto);
        return new ApiResponse<>(shop);
    }

    @GetMapping()
    public ApiResponse<Collection<ShopDto>> getShops(){
        return new ApiResponse<>(shopService.getShops());
    }

}
