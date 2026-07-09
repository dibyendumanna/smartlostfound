package com.smartlostfound.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class LostItemFormRequest {

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Location is required")
    private String location;

    private LocalDateTime lostDate;

    private MultipartFile image;
}