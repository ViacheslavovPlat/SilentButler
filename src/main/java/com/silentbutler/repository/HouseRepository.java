package com.silentbutler.repository;

import com.silentbutler.domain.House;
import com.silentbutler.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByUser(User user);
}