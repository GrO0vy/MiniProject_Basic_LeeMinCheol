package com.example.mutsaMarket.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "negotiation")
public class NegotiationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer suggestedPrice;
    private String status;
    @Column(nullable = false)
    private String writer;
    private String password;

    @ManyToOne
    @JoinColumn(name = "itemId", nullable = false)
    private SalesItemEntity salesItem;
}
