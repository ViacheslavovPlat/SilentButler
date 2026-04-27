package com.silentbutler.repository;

import com.silentbutler.domain.DeviceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceCategoryRepository extends JpaRepository<DeviceCategory, Long> {
    Optional<DeviceCategory> findByName(String name);
    boolean existsByName(String name);
}