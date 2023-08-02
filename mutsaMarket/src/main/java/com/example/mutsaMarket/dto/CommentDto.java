package com.example.mutsaMarket.dto;

import com.example.mutsaMarket.entity.CommentEntity;
import com.example.mutsaMarket.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    private Integer id;
    private Integer itemId;
    private String content;
    private String reply;
    private String userId;

    public static CommentDto fromEntity(CommentEntity entity){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(entity.getId());
        commentDto.setContent(entity.getContent());
        commentDto.setReply(entity.getReply());
        commentDto.setUserId(entity.getUser().getUserId());
        return commentDto;
    }
}
