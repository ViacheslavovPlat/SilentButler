package com.silentbutler.service;

import com.silentbutler.domain.Device;
import com.silentbutler.domain.Room;
import com.silentbutler.domain.User;
import com.silentbutler.dto.CreateDeviceRequest;
import com.silentbutler.dto.DeviceResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.DeviceRepository;
import com.silentbutler.repository.RoomRepository;
import com.silentbutler.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public DeviceService(DeviceRepository deviceRepository,
                         RoomRepository roomRepository,
                         UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    // ADMIN: текущую версию можно оставить без изменений
    public DeviceResponse createDevice(CreateDeviceRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));

        Device device = Device.builder()
                .name(request.getName())
                .type(request.getType())
                .status(request.isStatus())
                .room(room)
                .build();

        Device savedDevice = deviceRepository.save(device);
        return mapToDeviceResponse(savedDevice);
    }

    // USER: создать девайс только в моей комнате
    public DeviceResponse createDeviceForCurrentUser(CreateDeviceRequest request, String username) {
        User user = getUserByUsername(username);
        Room ownedRoom = getOwnedRoom(request.getRoomId(), user.getId());

        Device device = Device.builder()
                .name(request.getName())
                .type(request.getType())
                .status(request.isStatus())
                .room(ownedRoom)
                .build();

        Device savedDevice = deviceRepository.save(device);
        return mapToDeviceResponse(savedDevice);
    }

    // ADMIN: можно оставить
    public DeviceResponse getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));
        return mapToDeviceResponse(device);
    }

    // USER: если endpoint GET /api/devices/{id} остаётся пользовательским, добавь и этот метод
    public DeviceResponse getDeviceByIdForCurrentUser(Long id, String username) {
        User user = getUserByUsername(username);
        Device device = getOwnedDevice(id, user.getId());
        return mapToDeviceResponse(device);
    }

    // ADMIN: можно оставить
    public List<DeviceResponse> getDevicesByRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        return deviceRepository.findByRoom(room)
                .stream()
                .map(this::mapToDeviceResponse)
                .toList();
    }

    // USER: только устройства моей комнаты
    public List<DeviceResponse> getDevicesByRoomForCurrentUser(Long roomId, String username) {
        User user = getUserByUsername(username);
        Room ownedRoom = getOwnedRoom(roomId, user.getId());

        return deviceRepository.findByRoom(ownedRoom)
                .stream()
                .map(this::mapToDeviceResponse)
                .toList();
    }

    // ADMIN: можно оставить
    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));
        deviceRepository.delete(device);
    }

    // USER
    public void deleteDeviceForCurrentUser(Long id, String username) {
        User user = getUserByUsername(username);
        Device device = getOwnedDevice(id, user.getId());
        deviceRepository.delete(device);
    }

    // ADMIN: можно оставить
    public DeviceResponse toggleDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));

        device.setStatus(!device.isStatus());
        Device savedDevice = deviceRepository.save(device);
        return mapToDeviceResponse(savedDevice);
    }

    // USER
    public DeviceResponse toggleDeviceForCurrentUser(Long id, String username) {
        User user = getUserByUsername(username);
        Device device = getOwnedDevice(id, user.getId());

        device.setStatus(!device.isStatus());
        Device savedDevice = deviceRepository.save(device);
        return mapToDeviceResponse(savedDevice);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with username: " + username));
    }

    private Room getOwnedRoom(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        if (!room.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }

        return room;
    }

    private Device getOwnedDevice(Long deviceId, Long userId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + deviceId));

        if (!device.getRoom().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Device not found with id: " + deviceId);
        }

        return device;
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
