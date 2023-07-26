package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dto.SalesItemDto;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.SalesItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class SalesItemController {
    private final SalesItemService service;

    @PostMapping
    public ResponseEntity register(@RequestBody SalesItemDto salesItemDto){
        service.registerItem(salesItemDto);
        ResponseObject response = new ResponseObject();
        response.setMessage("등록이 완료되었습니다");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Page<SalesItemDto> readAll(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                                      @RequestParam(value = "size", defaultValue = Integer.MAX_VALUE + "" ) Integer pageSize
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

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{itemId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateImage(@PathVariable("itemId") Integer itemId,
                                      @RequestParam("image") MultipartFile image,
                                      @RequestParam("writer") String writer,
                                      @RequestParam("password") String password
                                      ){

        service.updateItemImage(itemId, image,writer,password);
        ResponseObject response = new ResponseObject();
        response.setMessage("물품이 수정되었습니다.");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity delete(@PathVariable("itemId") Integer itemId, @RequestBody SalesItemDto salesItemDto){

        service.deleteItem(itemId, salesItemDto);
        ResponseObject response = new ResponseObject();
        response.setMessage("물품을 삭제했습니다.");
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity dataIntegrityViolationError(DataIntegrityViolationException exception){
        log.error("모든 항목을 입력하지 않음");

        ResponseObject response = new ResponseObject();
        response.setMessage("필수 항목을 모두 입력해주세요");

        return ResponseEntity.badRequest().body(response);
    }
}
