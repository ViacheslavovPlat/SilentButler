package com.silentbutler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silentbutler.dto.CreateThermostatLogRequest;
import com.silentbutler.dto.ThermostatLogResponse;
import com.silentbutler.service.ThermostatLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ThermostatLogControllerTest {

    @Mock
    private ThermostatLogService thermostatLogService;

    @InjectMocks
    private ThermostatLogController thermostatLogController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createLog_shouldReturnCreatedLog() throws Exception {
        CreateThermostatLogRequest request = CreateThermostatLogRequest.builder()
                .temperature(22.5)
                .mode("HEATING")
                .build();

        ThermostatLogResponse response = ThermostatLogResponse.builder()
                .id(1L)
                .deviceId(1L)
                .deviceName("Test Thermostat")
                .temperature(22.5)
                .mode("HEATING")
                .createdAt(LocalDateTime.now())
                .build();

        when(thermostatLogService.createLog(eq(1L), any(CreateThermostatLogRequest.class)))
                .thenReturn(response);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(thermostatLogController).build();

        mockMvc.perform(post("/api/thermostats/{deviceId}/logs", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.temperature").value(22.5));
    }

    @Test
    void createLog_shouldReturn400WhenTemperatureNull() throws Exception {
        CreateThermostatLogRequest request = CreateThermostatLogRequest.builder()
                .mode("HEATING")
                .build();

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(thermostatLogController).build();

        mockMvc.perform(post("/api/thermostats/{deviceId}/logs", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLogs_shouldReturnList() throws Exception {
        ThermostatLogResponse log1 = ThermostatLogResponse.builder()
                .id(1L).deviceId(1L).temperature(22.0).createdAt(LocalDateTime.now()).build();
        ThermostatLogResponse log2 = ThermostatLogResponse.builder()
                .id(2L).deviceId(1L).temperature(23.0).createdAt(LocalDateTime.now()).build();

        when(thermostatLogService.getLogsByDevice(1L)).thenReturn(List.of(log2, log1));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(thermostatLogController).build();

        mockMvc.perform(get("/api/thermostats/{deviceId}/logs", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[1].id").value(1L));
    }

    @Test
    void getLatestLog_shouldReturnLatest() throws Exception {
        ThermostatLogResponse response = ThermostatLogResponse.builder()
                .id(1L).deviceId(1L).temperature(22.5).createdAt(LocalDateTime.now()).build();

        when(thermostatLogService.getLatestLogByDevice(1L)).thenReturn(response);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(thermostatLogController).build();

        mockMvc.perform(get("/api/thermostats/{deviceId}/logs/latest", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.temperature").value(22.5));
    }
}
