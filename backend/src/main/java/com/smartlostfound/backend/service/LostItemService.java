package com.smartlostfound.backend.service;

import com.smartlostfound.backend.dto.LostItemRequest;
import com.smartlostfound.backend.entity.LostItem;
import com.smartlostfound.backend.entity.User;
import com.smartlostfound.backend.repository.LostItemRepository;
import com.smartlostfound.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smartlostfound.backend.dto.LostItemResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LostItemService {

    @Autowired
    private LostItemRepository lostItemRepository;

    @Autowired
    private UserRepository userRepository;

    public LostItemResponse createLostItem(LostItemRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LostItem lostItem = new LostItem();

        lostItem.setItemName(request.getItemName());
        lostItem.setDescription(request.getDescription());
        lostItem.setCategory(request.getCategory());
        lostItem.setLocation(request.getLocation());
        lostItem.setLostDate(request.getLostDate());
        lostItem.setImageUrl(request.getImageUrl());
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
}