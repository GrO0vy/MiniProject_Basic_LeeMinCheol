package com.example.mutsaMarket.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String writer;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String content;
    private String reply;

    @ManyToOne
    @JoinColumn(name = "itemId", nullable = false)
    private SalesItemEntity salesItem;
}
