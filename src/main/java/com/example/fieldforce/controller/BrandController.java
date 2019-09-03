package com.example.fieldforce.controller;

import com.example.fieldforce.helper.AuthHelper;
import com.example.fieldforce.model.ApiResponse;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.BrandDto;
import com.example.fieldforce.model.Constant;
import com.example.fieldforce.serviceImpl.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired private HttpServletRequest request;
    @Autowired private BrandService brandService;

    @PostMapping
    public ApiResponse<BrandDto> addBrand(@RequestBody BrandDto brandDto){
        AuthUser authUser = AuthHelper.getAuthUser(request.getHeader(Constant.AUTH_CONSTANT));
        BrandDto brand = brandService.addBrand(brandDto, authUser);
        return new ApiResponse<>(brand);
    }

    @GetMapping
    public ApiResponse<Collection<BrandDto>> getAllBrands(){
        Collection<BrandDto> brandDtos = brandService.listAll();
        return new ApiResponse<>(brandDtos);
    }

}
