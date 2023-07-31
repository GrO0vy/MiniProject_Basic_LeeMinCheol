package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dto.SalesItemDto;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.SalesItemService;
import com.example.mutsaMarket.userManage.CustomUserDetails;
import com.example.mutsaMarket.userManage.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class SalesItemController {
    private final SalesItemService service;
    private final CustomUserDetailsManager manager;

    @PostMapping
    public ResponseEntity register(@RequestBody SalesItemDto salesItemDto, Authentication authentication){
        service.registerItem(salesItemDto, getLoginUser(authentication));
        ResponseObject response = new ResponseObject();
        response.setMessage("등록이 완료되었습니다");
        return ResponseEntity.ok(response);
    }

    // 쿼리 파라미터를 전달하지 않으면 기본 값인 Integer.MAX_VALUE 를 페이지 크기로 전달하여 페이지 전체 조회
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
    public ResponseEntity update(@PathVariable("itemId") Integer itemId, @RequestBody SalesItemDto salesItemDto, Authentication authentication){
        service.updateItem(itemId, salesItemDto, getLoginUser(authentication));
        ResponseObject response = new ResponseObject();
        response.setMessage("물품이 수정되었습니다");

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{itemId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateImage(@PathVariable("itemId") Integer itemId,
                                      @RequestParam("image") MultipartFile image,
                                      Authentication authentication
                                      ){
        service.updateItemImage(itemId, image,getLoginUser(authentication));
        ResponseObject response = new ResponseObject();
        response.setMessage("이미지가 등록되었습니다.");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity delete(
            @PathVariable("itemId") Integer itemId,
            Authentication authentication
    ){

        service.deleteItem(itemId, getLoginUser(authentication));
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

    public CustomUserDetails getLoginUser(Authentication authentication){
        String user = ((UserDetails)authentication.getPrincipal()).getUsername();
        return (CustomUserDetails) (manager.loadUserByUsername(user));
    }
}
