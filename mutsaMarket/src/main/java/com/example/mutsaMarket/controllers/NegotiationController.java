package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dto.NegotiationDto;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.NegotiationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
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
        int result = service.updateNegotiation(itemId, proposalId, negotiationDto);

        ResponseObject response = new ResponseObject();

        if(result == 1){
            response.setMessage("구매가 확정되었습니다");
        }
        else if(result == 2){
            response.setMessage("제안의 상태가 변경되었습니다");
        }
        else{
            response.setMessage("제안이 수정되었습니다.");
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{proposalId}")
    public ResponseEntity delete(@PathVariable("itemId") Integer itemId,
                                 @PathVariable("proposalId") Integer proposalId,
                                 @RequestBody NegotiationDto negotiationDto
    )
    {
        service.deleteNegotiation(itemId, proposalId, negotiationDto);

        ResponseObject response = new ResponseObject();
        response.setMessage("제안을 삭제했습니다.");

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity dataIntegrityViolationError(DataIntegrityViolationException exception){
        log.error("모든 항목을 입력하지 않음");

        ResponseObject response = new ResponseObject();
        response.setMessage("필수 항목을 모두 입력해주세요");

        return ResponseEntity.badRequest().body(response);
    }
}
