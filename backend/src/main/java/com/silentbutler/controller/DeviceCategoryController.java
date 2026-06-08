package com.silentbutler.controller;

import com.silentbutler.dto.CreateDeviceCategoryRequest;
import com.silentbutler.dto.DeviceCategoryResponse;
import com.silentbutler.service.DeviceCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device-categories")
public class DeviceCategoryController {

    private final DeviceCategoryService deviceCategoryService;

    public DeviceCategoryController(DeviceCategoryService deviceCategoryService) {
        this.deviceCategoryService = deviceCategoryService;
    }

    @PostMapping
    public ResponseEntity<DeviceCategoryResponse> createCategory(
            @Valid @RequestBody CreateDeviceCategoryRequest request) {
        return ResponseEntity.ok(deviceCategoryService.createCategory(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceCategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceCategoryService.getCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<DeviceCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(deviceCategoryService.getAllCategories());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        deviceCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}