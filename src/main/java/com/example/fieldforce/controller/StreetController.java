package com.example.fieldforce.controller;

import com.example.fieldforce.exception.FfaException;
import com.example.fieldforce.helper.AuthHelper;
import com.example.fieldforce.model.ApiResponse;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.Constant;
import com.example.fieldforce.model.StreetDto;
import com.example.fieldforce.serviceImpl.StreetService;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/street")
public class StreetController {

    @Autowired private HttpServletRequest request;
    @Autowired private StreetService streetService;

    @PostMapping
    public ApiResponse<StreetDto> addStreet(@RequestBody StreetDto streetDto){
        AuthUser authUser = AuthHelper.getAuthUser(request.getHeader(Constant.AUTH_CONSTANT));
         streetDto = streetService.createStreet(streetDto, authUser);
         return new ApiResponse<>(streetDto);
    }

    @GetMapping
    public ApiResponse<List<StreetDto>> getAllStreets(){
        List<StreetDto> streetDtos = streetService.listAll();
        if(CollectionUtils.isEmpty(streetDtos))
            throw new FfaException("No Data Found", "No Streets found");
        return new ApiResponse<>(streetDtos);
    }
}
