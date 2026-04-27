package com.silentbutler.service;

import com.silentbutler.domain.Device;
import com.silentbutler.domain.DeviceCategory;
import com.silentbutler.domain.Event;
import com.silentbutler.domain.Room;
import com.silentbutler.dto.CreateDeviceRequest;
import com.silentbutler.dto.DeviceResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.DeviceCategoryRepository;
import com.silentbutler.repository.DeviceRepository;
import com.silentbutler.repository.EventRepository;
import com.silentbutler.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;
    private final DeviceCategoryRepository deviceCategoryRepository;
    private final EventRepository eventRepository;

    public DeviceService(DeviceRepository deviceRepository,
                         RoomRepository roomRepository,
                         DeviceCategoryRepository deviceCategoryRepository,
                         EventRepository eventRepository) {
        this.deviceRepository = deviceRepository;
        this.roomRepository = roomRepository;
        this.deviceCategoryRepository = deviceCategoryRepository;
        this.eventRepository = eventRepository;
    }

    public DeviceResponse createDevice(CreateDeviceRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        DeviceCategory category = deviceCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Device device = Device.builder()
                .name(request.getName())
                .room(room)
                .category(category)
                .status(request.isStatus())
                .active(request.isActive())
                .createdAt(LocalDateTime.now())
                .build();

        deviceRepository.save(device);
        return mapToResponse(device);
    }

    public DeviceResponse getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        return mapToResponse(device);
    }

    public List<DeviceResponse> getDevicesByRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return deviceRepository.findByRoom(room)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DeviceResponse toggleDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));

        String oldState = device.isStatus() ? "ON" : "OFF";
        device.setStatus(!device.isStatus());
        device.setUpdatedAt(LocalDateTime.now());
        deviceRepository.save(device);
        String newState = device.isStatus() ? "ON" : "OFF";

        // automatically log the event
        Event event = Event.builder()
                .device(device)
                .eventType("TOGGLE")
                .oldState(oldState)
                .newState(newState)
                .timestamp(LocalDateTime.now())
                .build();
        eventRepository.save(event);

        return mapToResponse(device);
    }

    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        deviceRepository.delete(device);
    }

    private DeviceResponse mapToResponse(Device device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .name(device.getName())
                .categoryName(device.getCategory().getName())
                .roomId(device.getRoom().getId())
                .status(device.isStatus())
                .active(device.isActive())
                .build();
    }
}