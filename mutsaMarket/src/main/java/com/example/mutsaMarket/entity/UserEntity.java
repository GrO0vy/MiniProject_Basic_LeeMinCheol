package com.example.mutsaMarket.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPassword;

    private String phone;
    private String email;
    private String address;

    @OneToMany(mappedBy = "user")
    private List<SalesItemEntity> salesItems;

    @OneToMany
    private List<CommentEntity> comments;
}
