package com.smartlostfound.backend.controller;

import com.smartlostfound.backend.dto.LostItemRequest;
import com.smartlostfound.backend.dto.LostItemResponse;
import com.smartlostfound.backend.service.LostItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lost-items")
public class LostItemController {

    @Autowired
    private LostItemService lostItemService;

    @PostMapping
    public LostItemResponse createLostItem(
            @Valid @RequestBody LostItemRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return lostItemService.createLostItem(request, email);
    }

    @GetMapping
    public List<LostItemResponse> getAllLostItems() {
        return lostItemService.getAllLostItems();
    }

    @GetMapping("/my-items")
    public List<LostItemResponse> getMyLostItems(
            Authentication authentication) {

        return lostItemService.getMyLostItems(authentication.getName());
    }

    @GetMapping("/{id}")
    public LostItemResponse getLostItemById(@PathVariable Long id) {

        return lostItemService.getLostItemById(id);
    }

    @PutMapping("/{id}")
    public LostItemResponse updateLostItem(
            @PathVariable Long id,
            @Valid @RequestBody LostItemRequest request,
            Authentication authentication) {

        return lostItemService.updateLostItem(
                id,
                request,
                authentication.getName()
        );
    }
}