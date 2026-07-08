package com.smartlostfound.backend.founditem.controller;

import com.smartlostfound.backend.founditem.dto.FoundItemRequest;
import com.smartlostfound.backend.founditem.dto.FoundItemResponse;
import com.smartlostfound.backend.founditem.service.FoundItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/found-items")
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    @PostMapping
    public FoundItemResponse createFoundItem(
            @Valid @RequestBody FoundItemRequest request,
            Authentication authentication) {

        return foundItemService.createFoundItem(
                request,
                authentication.getName()
        );
    }

    @GetMapping
    public List<FoundItemResponse> getAllFoundItems() {
        return foundItemService.getAllFoundItems();
    }

    @GetMapping("/my-items")
    public List<FoundItemResponse> getMyFoundItems(
            Authentication authentication) {

        return foundItemService.getMyFoundItems(
                authentication.getName()
        );
    }

    @GetMapping("/{id}")
    public FoundItemResponse getFoundItemById(
            @PathVariable Long id) {

        return foundItemService.getFoundItemById(id);
    }

    @PutMapping("/{id}")
    public FoundItemResponse updateFoundItem(
            @PathVariable Long id,
            @Valid @RequestBody FoundItemRequest request,
            Authentication authentication) {

        return foundItemService.updateFoundItem(
                id,
                request,
                authentication.getName()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFoundItem(
            @PathVariable Long id,
            Authentication authentication) {

        foundItemService.deleteFoundItem(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok("Found item deleted successfully");
    }
}