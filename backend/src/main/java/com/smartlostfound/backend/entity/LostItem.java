package com.smartlostfound.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "lost_items")
public class LostItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    @Column(length = 1000)
    private String description;

    private String category;

    private String location;

    private LocalDateTime lostDate;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}