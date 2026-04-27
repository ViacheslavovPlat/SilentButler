package com.silentbutler.service;

import com.silentbutler.domain.DeviceCategory;
import com.silentbutler.dto.CreateDeviceCategoryRequest;
import com.silentbutler.dto.DeviceCategoryResponse;
import com.silentbutler.exception.ResourceAlreadyExistsException;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.DeviceCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceCategoryService {

    private final DeviceCategoryRepository deviceCategoryRepository;

    public DeviceCategoryService(DeviceCategoryRepository deviceCategoryRepository) {
        this.deviceCategoryRepository = deviceCategoryRepository;
    }

    public DeviceCategoryResponse createCategory(CreateDeviceCategoryRequest request) {
        if (deviceCategoryRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Category already exists");
        }

        DeviceCategory category = DeviceCategory.builder()
                .name(request.getName())
                .icon(request.getIcon())
                .description(request.getDescription())
                .build();

        deviceCategoryRepository.save(category);
        return mapToResponse(category);
    }

    public DeviceCategoryResponse getCategoryById(Long id) {
        DeviceCategory category = deviceCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return mapToResponse(category);
    }

    public List<DeviceCategoryResponse> getAllCategories() {
        return deviceCategoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteCategory(Long id) {
        DeviceCategory category = deviceCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        deviceCategoryRepository.delete(category);
    }

    private DeviceCategoryResponse mapToResponse(DeviceCategory category) {
        return DeviceCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .icon(category.getIcon())
                .description(category.getDescription())
                .build();
    }
}