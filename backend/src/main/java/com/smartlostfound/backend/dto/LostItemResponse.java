package com.smartlostfound.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LostItemResponse {

    private Long id;

    private String itemName;

    private String description;

    private String category;

    private String location;

    private LocalDateTime lostDate;

    private String imageUrl;

    private String reportedBy;
}