package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dao.SalesItemDao;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.SalesItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class SalesItemController {
    private final SalesItemService service;

    @PostMapping
    public ResponseEntity register(@RequestBody SalesItemDao salesItemDao){
        service.registerItem(salesItemDao);
        ResponseObject response = new ResponseObject();
        response.setMessage("등록이 완료되었습니다");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping({"/{itemId}"})
    public SalesItemDao readOne(@PathVariable("itemId") Integer itemId){
        return service.readItemById(itemId);
    }
}
