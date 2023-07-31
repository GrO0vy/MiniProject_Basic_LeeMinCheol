package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dto.NegotiationDto;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.NegotiationService;
import com.example.mutsaMarket.userManage.CustomUserDetails;
import com.example.mutsaMarket.userManage.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/proposals")
public class NegotiationController {
    private final NegotiationService service;
    private final CustomUserDetailsManager manager;

    @PostMapping
    public ResponseEntity register(@PathVariable("itemId") Integer itemId,
                                   @RequestBody NegotiationDto negotiationDto,
                                   Authentication authentication
    )
    {
        service.registerNegotiation(itemId, negotiationDto, getLoginUser(authentication));

        ResponseObject response = new ResponseObject();
        response.setMessage("구매 제안이 등록되었습니다.");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Page<NegotiationDto> readAll(@PathVariable("itemId") Integer itemId,
                                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                                        @RequestParam(value = "size", defaultValue = "2") Integer size,
                                        Authentication authentication
    )
    {
        return service.readNegotiationAll(itemId, page, size, getLoginUser(authentication));
    }

    @PutMapping("/{proposalId}")
    public ResponseEntity update(@PathVariable("itemId") Integer itemId,
                       @PathVariable("proposalId") Integer proposalId,
                       @RequestBody NegotiationDto negotiationDto,
                                 Authentication authentication
    )
    {
        String result = service.updateNegotiation(itemId, proposalId, negotiationDto, getLoginUser(authentication));

        ResponseObject response = new ResponseObject();
        response.setMessage(result);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{proposalId}")
    public ResponseEntity delete(@PathVariable("itemId") Integer itemId,
                                 @PathVariable("proposalId") Integer proposalId,
                                 Authentication authentication
    )
    {
        service.deleteNegotiation(itemId, proposalId, getLoginUser(authentication));

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

    public CustomUserDetails getLoginUser(Authentication authentication){
        String user = ((UserDetails)authentication.getPrincipal()).getUsername();
        return (CustomUserDetails) (manager.loadUserByUsername(user));
    }
}
