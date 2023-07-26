package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dto.CommentDto;
import com.example.mutsaMarket.dto.SalesItemDto;
import com.example.mutsaMarket.entity.CommentEntity;
import com.example.mutsaMarket.entity.SalesItemEntity;
import com.example.mutsaMarket.repositories.CommentRepository;
import com.example.mutsaMarket.repositories.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesItemService {
    private final SalesItemRepository salesItemRepository;
    private final CommentRepository commentRepository;

    public SalesItemDto registerItem(SalesItemDto salesItemDto){
        SalesItemEntity salesItemEntity = new SalesItemEntity();

        salesItemEntity.setTitle(salesItemDto.getTitle());
        salesItemEntity.setDescription(salesItemDto.getDescription());
        salesItemEntity.setMinPriceWanted(salesItemDto.getMinPriceWanted());
        salesItemEntity.setWriter(salesItemDto.getWriter());
        salesItemEntity.setPassword(salesItemDto.getPassword());

        salesItemEntity = salesItemRepository.save(salesItemEntity);

        return SalesItemDto.fromEntity(salesItemEntity);
    }

    public Page<SalesItemDto> readItemAll(Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        Page<SalesItemEntity> salesItemEntityPage = salesItemRepository.findAll(pageable);
        Page<SalesItemDto> salesItemDtoPage = salesItemEntityPage.map(SalesItemDto::fromEntity);

        return salesItemDtoPage;

    }
    public SalesItemDto readItemById(Integer itemId){
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if(optionalEntity.isPresent()){
            SalesItemEntity entity = optionalEntity.get();

            SalesItemDto salesItemDto = new SalesItemDto();
            salesItemDto.setTitle(entity.getTitle());
            salesItemDto.setDescription(entity.getDescription());
            salesItemDto.setMinPriceWanted(entity.getMinPriceWanted());
            salesItemDto.setStatus(entity.getStatus());

            return salesItemDto;
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public SalesItemDto updateItem(Integer itemId, SalesItemDto salesItemDto){
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if(!optionalEntity.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(!isValidUser(optionalEntity,salesItemDto.getWriter(), salesItemDto.getPassword())){
            log.info("작성자 정보가 일치하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        SalesItemEntity salesItemEntity = optionalEntity.get();

        salesItemEntity.setTitle(salesItemDto.getTitle());
        salesItemEntity.setDescription(salesItemDto.getDescription());
        salesItemEntity.setMinPriceWanted(salesItemDto.getMinPriceWanted());
        salesItemEntity.setWriter(salesItemDto.getWriter());
        salesItemEntity.setPassword(salesItemDto.getPassword());

        salesItemEntity = salesItemRepository.save(salesItemEntity);

        return SalesItemDto.fromEntity(salesItemEntity);
    }

    public SalesItemDto updateItemImage(Integer itemId, MultipartFile image, String writer, String password){
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if(!optionalEntity.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(!isValidUser(optionalEntity, writer, password)){
            log.info("작성자 정보가 일치하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        String imageDir = String.format("itemImages/%d",itemId);
        try{
            Files.createDirectories(Path.of(imageDir));
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.indexOf(".")+1);
        String imageFileName = "image." + extension;

        String fullPath = String.format("%s/%s",imageDir,imageFileName);

        try{
            image.transferTo(Path.of(fullPath));
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        SalesItemEntity salesItemEntity = optionalEntity.get();
        salesItemEntity.setImageUrl(String.format("static/%d/%s",itemId,imageFileName));

        salesItemEntity = salesItemRepository.save(salesItemEntity);

        return SalesItemDto.fromEntity(salesItemEntity);
    }

    public void deleteItem(Integer itemId, SalesItemDto salesItemDto){
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if(!optionalEntity.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(!isValidUser(optionalEntity,salesItemDto.getWriter(), salesItemDto.getPassword())){
            log.info("작성자 정보가 일치하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        salesItemRepository.delete(optionalEntity.get());
//        List<CommentEntity> commentEntityList = commentRepository.findAllByItemId(itemId);
//
//        for(var commentEntity : commentEntityList){
//            commentRepository.delete(commentEntity);
//        }

        commentRepository.deleteAllByItemId(itemId);
    }

    public boolean isValidUser(Optional<SalesItemEntity> optionalEntity, String writer, String password){
        SalesItemEntity entity = optionalEntity.get();
        return entity.getWriter().equals(writer) && entity.getPassword().equals(password);
    }
}
