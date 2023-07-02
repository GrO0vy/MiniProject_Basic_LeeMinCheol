package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dto.SalesItemDto;
import com.example.mutsaMarket.entity.SalesItemEntity;
import com.example.mutsaMarket.repositories.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public SalesItemDto registerItem(SalesItemDto salesItemDao){
        SalesItemEntity salesItemEntity = new SalesItemEntity();

        salesItemEntity.setTitle(salesItemDao.getTitle());
        salesItemEntity.setDescription(salesItemDao.getDescription());
        salesItemEntity.setMinPriceWanted(salesItemDao.getMinPriceWanted());
        salesItemEntity.setWriter(salesItemDao.getWriter());
        salesItemEntity.setPassword(salesItemDao.getPassword());

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
            return SalesItemDto.fromEntity(entity);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public SalesItemDto updateItem(Integer itemId, SalesItemDto salesItemDto){
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if(!optionalEntity.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(!isValidPassword(optionalEntity,salesItemDto.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

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

        if(!isValidPassword(optionalEntity,password))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);


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

        if(!isValidPassword(optionalEntity,salesItemDto.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        salesItemRepository.delete(optionalEntity.get());
    }

    public boolean isValidPassword(Optional<SalesItemEntity> optionalEntity, String password){
        return optionalEntity.get().getPassword().equals(password);
    }
}
