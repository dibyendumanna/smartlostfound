package com.smartlostfound.backend.service;

import com.smartlostfound.backend.dto.LostItemRequest;
import com.smartlostfound.backend.lostitem.entity.LostItem;
import com.smartlostfound.backend.entity.User;
import com.smartlostfound.backend.repository.LostItemRepository;
import com.smartlostfound.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smartlostfound.backend.dto.LostItemResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import com.smartlostfound.backend.exception.ResourceNotFoundException;
import com.smartlostfound.backend.exception.AccessDeniedException;
import com.smartlostfound.backend.dto.LostItemFormRequest;
import com.smartlostfound.backend.file.service.FileStorageService;

@Service
public class LostItemService {

    private final FileStorageService fileStorageService;
    private final LostItemRepository lostItemRepository;
    private final UserRepository userRepository;

    public LostItemService(
            LostItemRepository lostItemRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService) {

        this.lostItemRepository = lostItemRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    public LostItemResponse createLostItem(
            LostItemFormRequest request,
            String email) throws IOException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imageFileName = null;

        if (request.getImage() != null && !request.getImage().isEmpty()) {

            try {
                imageFileName = fileStorageService.saveFile(request.getImage());

            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        LostItem lostItem = new LostItem();

        lostItem.setItemName(request.getItemName());
        lostItem.setDescription(request.getDescription());
        lostItem.setCategory(request.getCategory());
        lostItem.setLocation(request.getLocation());
        lostItem.setLostDate(request.getLostDate());
        lostItem.setImageUrl(imageFileName);
        lostItem.setUser(user);

        LostItem savedItem = lostItemRepository.save(lostItem);

        return mapToResponse(savedItem);
    }

    public List<LostItemResponse> getAllLostItems() {

        return lostItemRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<LostItemResponse> getMyLostItems(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return lostItemRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LostItemResponse getLostItemById(Long id) {

        LostItem lostItem = lostItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lost item not found"));

        return mapToResponse(lostItem);
    }

    private LostItemResponse mapToResponse(LostItem lostItem) {

        LostItemResponse response = new LostItemResponse();

        response.setId(lostItem.getId());
        response.setItemName(lostItem.getItemName());
        response.setDescription(lostItem.getDescription());
        response.setCategory(lostItem.getCategory());
        response.setLocation(lostItem.getLocation());
        response.setLostDate(lostItem.getLostDate());
        response.setImageUrl(lostItem.getImageUrl());
        response.setReportedBy(lostItem.getUser().getEmail());

        return response;
    }

    public LostItemResponse updateLostItem(
            Long id,
            LostItemFormRequest request,
            String email) {

        LostItem lostItem = lostItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lost item not found"));

        // Check ownership
        if (!lostItem.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You can update only your own lost items");
        }

        lostItem.setItemName(request.getItemName());
        lostItem.setDescription(request.getDescription());
        lostItem.setCategory(request.getCategory());
        lostItem.setLocation(request.getLocation());
        lostItem.setLostDate(request.getLostDate());

// Handle image replacement
        if (request.getImage() != null && !request.getImage().isEmpty()) {

            // Delete old image
            fileStorageService.deleteFile(lostItem.getImageUrl());

            try {
                String newImage =
                        fileStorageService.saveFile(request.getImage());

                lostItem.setImageUrl(newImage);

            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        LostItem updatedItem = lostItemRepository.save(lostItem);

        return mapToResponse(updatedItem);
    }

    public void deleteLostItem(Long id, String email) {

        LostItem lostItem = lostItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lost item not found"));

        if (!lostItem.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You can delete only your own lost items");
        }

        // Delete image from uploads folder
        fileStorageService.deleteFile(lostItem.getImageUrl());

        // Delete database record
        lostItemRepository.delete(lostItem);
    }
}