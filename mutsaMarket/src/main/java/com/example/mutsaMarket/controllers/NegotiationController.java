package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dto.NegotiationDto;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.NegotiationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/proposals")
public class NegotiationController {
    private final NegotiationService service;

    @PostMapping
    public ResponseEntity register(@PathVariable("itemId") Integer itemId,
                                   @RequestBody NegotiationDto negotiationDto
    )
    {
        service.registerNegotiation(itemId, negotiationDto);

        ResponseObject response = new ResponseObject();
        response.setMessage("구매 제안이 등록되었습니다.");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Page<NegotiationDto> readAll(@PathVariable("itemId") Integer itemId,
                                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                                        @RequestParam(value = "size", defaultValue = "2") Integer size,
                                        @RequestParam(value = "writer") String writer,
                                        @RequestParam(value = "password") String password
    )
    {
        return service.readNegotiationAll(itemId, page, size, writer, password);
    }

    @PutMapping("/{proposalId}")
    public ResponseEntity update(@PathVariable("itemId") Integer itemId,
                       @PathVariable("proposalId") Integer proposalId,
                       @RequestBody NegotiationDto negotiationDto
    )
    {
        service.updateNegotiation(itemId, proposalId, negotiationDto);

        ResponseObject response = new ResponseObject();
        response.setMessage("제안이 수정되었습니다.");

        return ResponseEntity.ok(response);
    }
}
