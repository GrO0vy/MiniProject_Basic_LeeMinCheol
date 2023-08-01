package com.example.mutsaMarket.controllers;

import com.example.mutsaMarket.dto.CommentDto;
import com.example.mutsaMarket.responses.ResponseObject;
import com.example.mutsaMarket.services.CommentService;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items/{itemId}/comments")
public class CommentController {
    private final CommentService service;
    private final CustomUserDetailsManager manager;

    @PostMapping
    public ResponseEntity register(
            @PathVariable("itemId") Integer itemId,
            @RequestBody CommentDto commentDto,
            Authentication authentication)
    {
        service.registerComment(itemId, commentDto, getLoginUser(authentication));

        ResponseObject response = new ResponseObject();
        response.setMessage("댓글이 등록되었습니다.");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Page<CommentDto> readAll(@PathVariable("itemId") Integer itemId,
                                    @RequestParam(value= "page", defaultValue = "0") Integer pageNumber,
                                    @RequestParam(value= "size", defaultValue = "25") Integer pageSize)
    {
        return service.readCommentAll(itemId,pageNumber,pageSize);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity update(@PathVariable("itemId") Integer itemId,
                              @PathVariable("commentId") Integer commentId,
                              @RequestBody CommentDto commentDto,
                                 Authentication authentication)
    {
        String message = service.updateComment(itemId, commentId, commentDto, getLoginUser(authentication));

        ResponseObject response = new ResponseObject();
        response.setMessage(message);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity delete(@PathVariable("itemId") Integer itemId,
                                 @PathVariable("commentId") Integer commentId,
                                 Authentication authentication)
    {
        service.deleteComment(itemId, commentId, getLoginUser(authentication));

        ResponseObject response = new ResponseObject();
        response.setMessage("댓글을 삭제했습니다.");

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
