package com.silentbutler.service;

import com.silentbutler.domain.Device;
import com.silentbutler.domain.Room;
import com.silentbutler.dto.CreateDeviceRequest;
import com.silentbutler.dto.DeviceResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.DeviceRepository;
import com.silentbutler.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;

    public DeviceService(DeviceRepository deviceRepository, RoomRepository roomRepository) {
        this.deviceRepository = deviceRepository;
        this.roomRepository = roomRepository;
    }

    public DeviceResponse createDevice(CreateDeviceRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Device device = Device.builder()
                .name(request.getName())
                .type(request.getType())
                .status(request.isStatus())
                .room(room)
                .build();

        deviceRepository.save(device);
        return mapToDeviceResponse(device);
    }

    public DeviceResponse getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        return mapToDeviceResponse(device);
    }

    public List<DeviceResponse> getDevicesByRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return deviceRepository.findByRoom(room)
                .stream()
                .map(this::mapToDeviceResponse)
                .toList();
    }

    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        deviceRepository.delete(device);
    }

    public DeviceResponse toggleDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        device.setStatus(!device.isStatus()); // flip on/off
        deviceRepository.save(device);
        return mapToDeviceResponse(device);
    }

    private DeviceResponse mapToDeviceResponse(Device device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .name(device.getName())
                .type(device.getType())
                .status(device.isStatus())
                .roomId(device.getRoom().getId())
                .build();
    }
}