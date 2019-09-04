package com.example.fieldforce.controller;

import com.example.fieldforce.helper.AuthHelper;
import com.example.fieldforce.model.ApiResponse;
import com.example.fieldforce.model.AuthUser;
import com.example.fieldforce.model.Constant;
import com.example.fieldforce.model.ItemDto;
import com.example.fieldforce.serviceImpl.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired private HttpServletRequest httpServletRequest;
    @Autowired private ItemService itemService;
    private ItemDto itemDto;

    @PostMapping
    public ApiResponse<ItemDto> addItem(@RequestBody ItemDto itemDto){
        AuthUser authUser = AuthHelper.getAuthUser(httpServletRequest.getHeader(Constant.AUTH_CONSTANT));
        return new ApiResponse<>(itemService.addItem(itemDto, authUser));
    }

    @GetMapping
    public ApiResponse<List<ItemDto>> getAll(){
        return new ApiResponse<>(itemService.getAll());
    }
}
