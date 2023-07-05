package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dto.NegotiationDto;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.NegotiationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/proposals")
public class NegotiationController {
    private final NegotiationService service;

    @PostMapping
    public ResponseEntity register(@PathVariable("itemId") Integer itemId, @RequestBody NegotiationDto negotiationDto){
        service.registerNegotiation(itemId, negotiationDto);

        ResponseObject response = new ResponseObject();
        response.setMessage("구매 제안이 등록되었습니다.");

        return ResponseEntity.ok(response);
    }
}
