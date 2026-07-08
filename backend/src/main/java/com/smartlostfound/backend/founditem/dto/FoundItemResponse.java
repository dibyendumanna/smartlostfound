package com.smartlostfound.backend.founditem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FoundItemResponse {

    private Long id;

    private String itemName;

    private String description;

    private String category;

    private String location;

    private LocalDateTime foundDate;

    private String imageUrl;

    private String reportedBy;
}