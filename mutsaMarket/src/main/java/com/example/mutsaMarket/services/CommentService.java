package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dto.CommentDto;
import com.example.mutsaMarket.entity.CommentEntity;
import com.example.mutsaMarket.repositories.CommentRepository;
import com.example.mutsaMarket.repositories.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final SalesItemRepository salesItemRepository;

    public CommentDto registerComment(Integer itemId, CommentDto commentDto){
        if(!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        CommentEntity entity = new CommentEntity();
        entity.setWriter(commentDto.getWriter());
        entity.setPassword(commentDto.getPassword());
        entity.setContent(commentDto.getContent());

        entity = commentRepository.save(entity);

        return CommentDto.fromEntity(entity);
    }
}
