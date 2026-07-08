package com.smartlostfound.backend.founditem.repository;

import com.smartlostfound.backend.entity.User;
import com.smartlostfound.backend.founditem.entity.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {

    List<FoundItem> findByUser(User user);

}