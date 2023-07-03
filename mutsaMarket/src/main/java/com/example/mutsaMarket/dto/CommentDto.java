package com.example.mutsaMarket.dto;

import com.example.mutsaMarket.entity.CommentEntity;
import lombok.Data;

@Data
public class CommentDto {
    private int id;
    private int itemId;
    private String writer;
    private String password;
    private String content;
    private String reply;

    public static CommentDto fromEntity(CommentEntity entity){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(entity.getId());
        commentDto.setContent(entity.getContent());
        commentDto.setReply(entity.getReply());

        return commentDto;
    }
}
