package com.silentbutler.controller;

import com.silentbutler.dto.CreateDeviceRequest;
import com.silentbutler.dto.DeviceResponse;
import com.silentbutler.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceResponse> createDevice(@Valid @RequestBody CreateDeviceRequest request,
                                                       Authentication authentication) {
        return ResponseEntity.ok(deviceService.createDeviceForCurrentUser(request, authentication.getName()));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<DeviceResponse>> getDevicesByRoom(@PathVariable Long roomId,
                                                                 Authentication authentication) {
        return ResponseEntity.ok(deviceService.getDevicesByRoomForCurrentUser(roomId, authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable Long id,
                                                        Authentication authentication) {
        return ResponseEntity.ok(deviceService.getDeviceByIdForCurrentUser(id, authentication.getName()));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<DeviceResponse> toggleDevice(@PathVariable Long id,
                                                       Authentication authentication) {
        return ResponseEntity.ok(deviceService.toggleDeviceForCurrentUser(id, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id,
                                             Authentication authentication) {
        deviceService.deleteDeviceForCurrentUser(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}