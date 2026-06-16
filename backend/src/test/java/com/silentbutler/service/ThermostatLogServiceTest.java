package com.silentbutler.service;

import com.silentbutler.domain.Device;
import com.silentbutler.domain.House;
import com.silentbutler.domain.Room;
import com.silentbutler.domain.ThermostatLog;
import com.silentbutler.domain.User;
import com.silentbutler.dto.CreateThermostatLogRequest;
import com.silentbutler.dto.ThermostatLogResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.DeviceRepository;
import com.silentbutler.repository.ThermostatLogRepository;
import com.silentbutler.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThermostatLogServiceTest {

    @Mock
    private ThermostatLogRepository thermostatLogRepository;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private UserRepository userRepository;

    private ThermostatLogService thermostatLogService;

    private User currentUser;
    private Device device;
    private House house;
    private Room room;

    @BeforeEach
    void setUp() {
        thermostatLogService = new ThermostatLogService(
                thermostatLogRepository, deviceRepository, userRepository);

        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("testuser");

        house = new House();
        house.setId(1L);
        house.setUser(currentUser);

        room = new Room();
        room.setId(1L);
        room.setHouse(house);

        device = new Device();
        device.setId(1L);
        device.setRoom(room);
        device.setName("Test Thermostat");

        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.getName()).thenReturn("testuser");
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        lenient().when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(currentUser));
    }

    @Test
    void createLog_shouldSaveAndReturnResponse() {
        CreateThermostatLogRequest request = CreateThermostatLogRequest.builder()
                .temperature(22.5)
                .targetTemperature(24.0)
                .humidity(55.0)
                .mode("HEATING")
                .status("ON")
                .batteryLevel(85)
                .build();

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        ThermostatLog savedLog = ThermostatLog.builder()
                .id(1L)
                .device(device)
                .temperature(22.5)
                .targetTemperature(24.0)
                .humidity(55.0)
                .mode("HEATING")
                .status("ON")
                .batteryLevel(85)
                .createdAt(LocalDateTime.now())
                .build();

        when(thermostatLogRepository.save(any(ThermostatLog.class))).thenReturn(savedLog);

        ThermostatLogResponse response = thermostatLogService.createLog(1L, request);

        assertThat(response.getDeviceId()).isEqualTo(1L);
        assertThat(response.getTemperature()).isEqualTo(22.5);
        assertThat(response.getTargetTemperature()).isEqualTo(24.0);
        assertThat(response.getHumidity()).isEqualTo(55.0);
        assertThat(response.getMode()).isEqualTo("HEATING");
        assertThat(response.getStatus()).isEqualTo("ON");
        assertThat(response.getBatteryLevel()).isEqualTo(85);

        verify(thermostatLogRepository).save(any(ThermostatLog.class));
    }

    @Test
    void createLog_shouldThrowWhenDeviceNotFound() {
        when(deviceRepository.findById(99L)).thenReturn(Optional.empty());

        CreateThermostatLogRequest request = CreateThermostatLogRequest.builder()
                .temperature(22.5)
                .build();

        assertThatThrownBy(() -> thermostatLogService.createLog(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Device not found");
    }

    @Test
    void createLog_shouldThrowWhenAccessDenied() {
        User otherUser = new User();
        otherUser.setId(2L);

        House otherHouse = new House();
        otherHouse.setId(2L);
        otherHouse.setUser(otherUser);

        Room otherRoom = new Room();
        otherRoom.setId(2L);
        otherRoom.setHouse(otherHouse);

        Device otherDevice = new Device();
        otherDevice.setId(2L);
        otherDevice.setRoom(otherRoom);

        when(deviceRepository.findById(2L)).thenReturn(Optional.of(otherDevice));

        CreateThermostatLogRequest request = CreateThermostatLogRequest.builder()
                .temperature(22.5)
                .build();

        assertThatThrownBy(() -> thermostatLogService.createLog(2L, request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("You do not have access to this device");
    }

    @Test
    void getLogsByDevice_shouldReturnSortedLogs() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        ThermostatLog log1 = ThermostatLog.builder()
                .id(1L).device(device).temperature(22.0)
                .createdAt(LocalDateTime.now().minusMinutes(5)).build();

        ThermostatLog log2 = ThermostatLog.builder()
                .id(2L).device(device).temperature(23.0)
                .createdAt(LocalDateTime.now()).build();

        when(thermostatLogRepository.findByDeviceOrderByCreatedAtDesc(device))
                .thenReturn(List.of(log2, log1));

        List<ThermostatLogResponse> responses = thermostatLogService.getLogsByDevice(1L);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(2L);
        assertThat(responses.get(1).getId()).isEqualTo(1L);
    }

    @Test
    void getLatestLogByDevice_shouldReturnMostRecent() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        ThermostatLog latestLog = ThermostatLog.builder()
                .id(1L).device(device).temperature(22.5)
                .createdAt(LocalDateTime.now()).build();

        when(thermostatLogRepository.findFirstByDeviceOrderByCreatedAtDesc(device))
                .thenReturn(Optional.of(latestLog));

        ThermostatLogResponse response = thermostatLogService.getLatestLogByDevice(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTemperature()).isEqualTo(22.5);
    }

    @Test
    void getLatestLogByDevice_shouldThrowWhenNoLogs() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(thermostatLogRepository.findFirstByDeviceOrderByCreatedAtDesc(device))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> thermostatLogService.getLatestLogByDevice(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No logs found for this device");
    }
}
