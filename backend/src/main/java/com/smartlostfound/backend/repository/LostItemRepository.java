package com.smartlostfound.backend.repository;

import com.smartlostfound.backend.entity.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.smartlostfound.backend.entity.User;
import java.util.List;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    List<LostItem> findByUser(User user);
}