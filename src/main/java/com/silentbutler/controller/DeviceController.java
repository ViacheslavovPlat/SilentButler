package com.silentbutler.controller;

import com.silentbutler.dto.CreateDeviceRequest;
import com.silentbutler.dto.DeviceResponse;
import com.silentbutler.service.DeviceService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceResponse> createDevice(@Valid @RequestBody CreateDeviceRequest request) {
        return ResponseEntity.ok(deviceService.createDevice(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<DeviceResponse>> getDevicesByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(deviceService.getDevicesByRoom(roomId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<DeviceResponse> toggleDevice(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.toggleDevice(id));
    }
}