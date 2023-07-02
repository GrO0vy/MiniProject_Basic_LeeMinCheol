package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dto.SalesItemDto;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.SalesItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class SalesItemController {
    private final SalesItemService service;

    @PostMapping
    public ResponseEntity register(@RequestBody SalesItemDto salesItemDao){
        service.registerItem(salesItemDao);
        ResponseObject response = new ResponseObject();
        response.setMessage("등록이 완료되었습니다");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public Page<SalesItemDto> readAll(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
                                      @RequestParam(value = "limit", defaultValue = "10") Integer pageSize
                                      )
    {
        return service.readItemAll(pageNumber, pageSize);
    }

    @GetMapping({"/{itemId}"})
    public SalesItemDto readOne(@PathVariable("itemId") Integer itemId){
        return service.readItemById(itemId);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity update(@PathVariable("itemId") Integer itemId, @RequestBody SalesItemDto salesItemDto){

        service.updateItem(itemId, salesItemDto);
        ResponseObject response = new ResponseObject();
        response.setMessage("물품이 수정되었습니다");

        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/{itemId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateImage(@PathVariable("itemId") Integer itemId,
                                      @RequestBody MultipartFile image){

        service.updateItemImage(itemId, image);
        ResponseObject response = new ResponseObject();
        response.setMessage("물품이 수정되었습니다.");

        return ResponseEntity.ok().body(response);
    }
}
