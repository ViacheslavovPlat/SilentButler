package com.silentbutler.service;

import com.silentbutler.domain.Device;
import com.silentbutler.domain.ThermostatLog;
import com.silentbutler.domain.User;
import com.silentbutler.dto.CreateThermostatLogRequest;
import com.silentbutler.dto.ThermostatLogResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.DeviceRepository;
import com.silentbutler.repository.ThermostatLogRepository;
import com.silentbutler.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThermostatLogService {

    private final ThermostatLogRepository thermostatLogRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public ThermostatLogService(ThermostatLogRepository thermostatLogRepository,
                                DeviceRepository deviceRepository,
                                UserRepository userRepository) {
        this.thermostatLogRepository = thermostatLogRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    public ThermostatLogResponse createLog(Long deviceId, CreateThermostatLogRequest request) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        assertDeviceOwnership(device);

        ThermostatLog log = ThermostatLog.builder()
                .device(device)
                .temperature(request.getTemperature())
                .targetTemperature(request.getTargetTemperature())
                .humidity(request.getHumidity())
                .mode(request.getMode())
                .status(request.getStatus())
                .batteryLevel(request.getBatteryLevel())
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(thermostatLogRepository.save(log));
    }

    public List<ThermostatLogResponse> getLogsByDevice(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        assertDeviceOwnership(device);

        return thermostatLogRepository.findByDeviceOrderByCreatedAtDesc(device)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ThermostatLogResponse getLatestLogByDevice(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        assertDeviceOwnership(device);

        ThermostatLog log = thermostatLogRepository.findFirstByDeviceOrderByCreatedAtDesc(device)
                .orElseThrow(() -> new ResourceNotFoundException("No logs found for this device"));

        return mapToResponse(log);
    }

    private void assertDeviceOwnership(Device device) {
        User currentUser = resolveCurrentUser();
        if (!device.getRoom().getHouse().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have access to this device");
        }
    }

    private User resolveCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ThermostatLogResponse mapToResponse(ThermostatLog log) {
        return ThermostatLogResponse.builder()
                .id(log.getId())
                .deviceId(log.getDevice().getId())
                .deviceName(log.getDevice().getName())
                .temperature(log.getTemperature())
                .targetTemperature(log.getTargetTemperature())
                .humidity(log.getHumidity())
                .mode(log.getMode())
                .status(log.getStatus())
                .batteryLevel(log.getBatteryLevel())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
