package com.example.mutsaMarket.services;

import com.example.mutsaMarket.dto.CommentDto;
import com.example.mutsaMarket.dto.SalesItemDto;
import com.example.mutsaMarket.entity.CommentEntity;
import com.example.mutsaMarket.entity.SalesItemEntity;
import com.example.mutsaMarket.entity.UserEntity;
import com.example.mutsaMarket.repositories.CommentRepository;
import com.example.mutsaMarket.repositories.NegotiationRepository;
import com.example.mutsaMarket.repositories.SalesItemRepository;
import com.example.mutsaMarket.repositories.UserRepository;
import com.example.mutsaMarket.userManage.CustomUserDetails;
import com.example.mutsaMarket.userManage.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final NegotiationRepository negotiationRepository;
    private final UserRepository userRepository;
    private final CustomUserDetailsManager manager;

    public SalesItemDto registerItem(SalesItemDto salesItemDto, CustomUserDetails user) {
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        SalesItemEntity salesItemEntity = new SalesItemEntity();

        salesItemEntity.setTitle(salesItemDto.getTitle());
        salesItemEntity.setDescription(salesItemDto.getDescription());
        salesItemEntity.setMinPriceWanted(salesItemDto.getMinPriceWanted());
        UserEntity userEntity = userRepository.findByUserId(user.getUsername()).get();
        salesItemEntity.setUser(userEntity);

        salesItemEntity = salesItemRepository.save(salesItemEntity);

        return SalesItemDto.fromEntity(salesItemEntity);
    }

    public Page<SalesItemDto> readItemAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<SalesItemEntity> salesItemEntityPage = salesItemRepository.findAll(pageable);
        Page<SalesItemDto> salesItemDtoPage = salesItemEntityPage.map(SalesItemDto::fromEntity);

        return salesItemDtoPage;

    }

    public SalesItemDto readItemById(Integer itemId) {
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if (optionalEntity.isPresent()) {
            SalesItemEntity entity = optionalEntity.get();

            SalesItemDto salesItemDto = new SalesItemDto();
            salesItemDto.setTitle(entity.getTitle());
            salesItemDto.setDescription(entity.getDescription());
            salesItemDto.setImageUrl(entity.getImageUrl());
            salesItemDto.setMinPriceWanted(entity.getMinPriceWanted());
            salesItemDto.setStatus(entity.getStatus());

            return salesItemDto;
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public SalesItemDto updateItem(Integer itemId, SalesItemDto salesItemDto, CustomUserDetails user) {
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if (!optionalEntity.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        if (!isValidUser(optionalEntity, user.getUsername(), user.getPassword())) {
            log.info("작성자 정보가 일치하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        SalesItemEntity salesItemEntity = optionalEntity.get();

        salesItemEntity.setTitle(salesItemDto.getTitle());
        salesItemEntity.setDescription(salesItemDto.getDescription());
        salesItemEntity.setMinPriceWanted(salesItemDto.getMinPriceWanted());
        UserEntity userEntity = userRepository.findByUserId(user.getUsername()).get();
        salesItemEntity.setUser(userEntity);

        salesItemEntity = salesItemRepository.save(salesItemEntity);

        return SalesItemDto.fromEntity(salesItemEntity);
    }

    public SalesItemDto updateItemImage(Integer itemId, MultipartFile image, CustomUserDetails user) {
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if (!optionalEntity.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if (!isValidUser(optionalEntity, user.getUsername(), user.getPassword())) {
            log.info("작성자 정보가 일치하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        String imageDir = String.format("itemImages/%d", itemId);
        try {
            Files.createDirectories(Path.of(imageDir));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.indexOf(".") + 1);
        String imageFileName = "image." + extension;

        String fullPath = String.format("%s/%s", imageDir, imageFileName);

        try {
            image.transferTo(Path.of(fullPath));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        SalesItemEntity salesItemEntity = optionalEntity.get();
        salesItemEntity.setImageUrl(String.format("static/%d/%s", itemId, imageFileName));

        salesItemEntity = salesItemRepository.save(salesItemEntity);

        return SalesItemDto.fromEntity(salesItemEntity);
    }

    public void deleteItem(Integer itemId, CustomUserDetails user) {
        Optional<SalesItemEntity> optionalEntity = salesItemRepository.findById(itemId);

        if (!optionalEntity.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        if (!isValidUser(optionalEntity, user.getUsername(), user.getPassword())) {
            log.info("작성자 정보가 일치하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        SalesItemEntity entity = optionalEntity.get();

        // 이미지 삭제
        if (entity.getImageUrl() != null) {
            try {
                // 파일 삭제
                String file = entity.getImageUrl();
                file = file.replace("static", "itemImages");
                // 디렉토리 삭제
                Files.deleteIfExists(Path.of(file));
                String directory = file.substring(0, file.indexOf("/image.png"));
                Files.deleteIfExists(Path.of(directory));
            } catch (Exception e) {
                log.warn(e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


        commentRepository.deleteAllBySalesItem(entity);
        negotiationRepository.deleteAllBySalesItem(entity);
        salesItemRepository.delete(entity);
    }

    public boolean isValidUser(Optional<SalesItemEntity> optionalEntity, String writer, String password) {
        SalesItemEntity entity = optionalEntity.get();
        return entity.getUser().getUserId().equals(writer) && entity.getUser().getUserPassword().equals(password);
    }
}
