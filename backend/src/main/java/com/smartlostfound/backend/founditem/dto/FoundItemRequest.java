package com.smartlostfound.backend.founditem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FoundItemRequest {

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Found date is required")
    private LocalDateTime foundDate;

    private String imageUrl;
}