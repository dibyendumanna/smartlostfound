package com.smartlostfound.backend.service;

import com.smartlostfound.backend.dto.LostItemRequest;
import com.smartlostfound.backend.entity.LostItem;
import com.smartlostfound.backend.entity.User;
import com.smartlostfound.backend.repository.LostItemRepository;
import com.smartlostfound.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LostItemService {

    @Autowired
    private LostItemRepository lostItemRepository;

    @Autowired
    private UserRepository userRepository;

    public LostItem createLostItem(LostItemRequest request, String email) {

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

        return lostItemRepository.save(lostItem);
    }
}