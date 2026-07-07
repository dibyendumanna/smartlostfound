package com.smartlostfound.backend.controller;

import com.smartlostfound.backend.dto.LostItemRequest;
import com.smartlostfound.backend.entity.LostItem;
import com.smartlostfound.backend.service.LostItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lost-items")
public class LostItemController {

    @Autowired
    private LostItemService lostItemService;

    @PostMapping
    public LostItem createLostItem(
            @Valid @RequestBody LostItemRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return lostItemService.createLostItem(request, email);
    }
}