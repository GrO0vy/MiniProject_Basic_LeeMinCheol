package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dto.CommentDto;
import com.example.mutsaMarket.entity.CommentEntity;
import com.example.mutsaMarket.repositories.CommentRepository;
import com.example.mutsaMarket.repositories.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    public Page<CommentDto> readCommentAll(Integer itemId, Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CommentEntity> commentEntityPage = commentRepository.findAll(pageable);

        Page<CommentDto> commentDtoPage = commentEntityPage.map(CommentDto::fromEntity);

        return commentDtoPage;
    }

    public CommentDto updateComment(Integer itemId, Integer commentId, CommentDto commentDto){
        if(!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Optional<CommentEntity> optionalEntity = commentRepository.findById(commentId);

        if(!optionalEntity.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        CommentEntity entity = optionalEntity.get();

        if(!entity.getPassword().equals(commentDto.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        entity.setWriter(commentDto.getWriter());
        entity.setContent(commentDto.getContent());

        entity = commentRepository.save(entity);

        return CommentDto.fromEntity(entity);
    }
}
