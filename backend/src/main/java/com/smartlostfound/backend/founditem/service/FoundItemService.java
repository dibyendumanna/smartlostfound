package com.smartlostfound.backend.founditem.service;

import com.smartlostfound.backend.entity.User;
import com.smartlostfound.backend.founditem.dto.FoundItemRequest;
import com.smartlostfound.backend.founditem.dto.FoundItemResponse;
import com.smartlostfound.backend.founditem.entity.FoundItem;
import com.smartlostfound.backend.founditem.repository.FoundItemRepository;
import com.smartlostfound.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smartlostfound.backend.exception.AccessDeniedException;
import com.smartlostfound.backend.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class FoundItemService {

    private final FoundItemRepository foundItemRepository;
    private final UserRepository userRepository;

    public FoundItemService(FoundItemRepository foundItemRepository, UserRepository userRepository) {
        this.foundItemRepository = foundItemRepository;
        this.userRepository = userRepository;
    }

    private FoundItemResponse mapToResponse(FoundItem foundItem) {

        FoundItemResponse response = new FoundItemResponse();

        response.setId(foundItem.getId());
        response.setItemName(foundItem.getItemName());
        response.setDescription(foundItem.getDescription());
        response.setCategory(foundItem.getCategory());
        response.setLocation(foundItem.getLocation());
        response.setFoundDate(foundItem.getFoundDate());
        response.setImageUrl(foundItem.getImageUrl());
        response.setReportedBy(foundItem.getUser().getEmail());

        return response;
    }

    public FoundItemResponse createFoundItem(
            FoundItemRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FoundItem foundItem = new FoundItem();

        foundItem.setItemName(request.getItemName());
        foundItem.setDescription(request.getDescription());
        foundItem.setCategory(request.getCategory());
        foundItem.setLocation(request.getLocation());
        foundItem.setFoundDate(request.getFoundDate());
        foundItem.setImageUrl(request.getImageUrl());
        foundItem.setUser(user);

        FoundItem savedItem = foundItemRepository.save(foundItem);

        return mapToResponse(savedItem);
    }

    public List<FoundItemResponse> getAllFoundItems() {

        return foundItemRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<FoundItemResponse> getMyFoundItems(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return foundItemRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FoundItemResponse getFoundItemById(Long id) {

        FoundItem foundItem = foundItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Found item not found"));

        return mapToResponse(foundItem);
    }

    public FoundItemResponse updateFoundItem(
            Long id,
            FoundItemRequest request,
            String email) {

        FoundItem foundItem = foundItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Found item not found"));

        if (!foundItem.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You can update only your own found items");
        }

        foundItem.setItemName(request.getItemName());
        foundItem.setDescription(request.getDescription());
        foundItem.setCategory(request.getCategory());
        foundItem.setLocation(request.getLocation());
        foundItem.setFoundDate(request.getFoundDate());
        foundItem.setImageUrl(request.getImageUrl());

        return mapToResponse(foundItemRepository.save(foundItem));
    }

    public void deleteFoundItem(Long id, String email) {

        FoundItem foundItem = foundItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Found item not found"));

        if (!foundItem.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You can delete only your own found items");
        }

        foundItemRepository.delete(foundItem);
    }
}