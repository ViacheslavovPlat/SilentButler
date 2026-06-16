package com.silentbutler.controller;

import com.silentbutler.dto.CreateThermostatLogRequest;
import com.silentbutler.dto.ThermostatLogResponse;
import com.silentbutler.service.ThermostatLogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thermostats")
public class ThermostatLogController {

    private final ThermostatLogService thermostatLogService;

    public ThermostatLogController(ThermostatLogService thermostatLogService) {
        this.thermostatLogService = thermostatLogService;
    }

    @PostMapping("/{deviceId}/logs")
    public ResponseEntity<ThermostatLogResponse> createLog(
            @PathVariable Long deviceId,
            @Valid @RequestBody CreateThermostatLogRequest request) {
        return ResponseEntity.ok(thermostatLogService.createLog(deviceId, request));
    }

    @GetMapping("/{deviceId}/logs")
    public ResponseEntity<List<ThermostatLogResponse>> getLogs(@PathVariable Long deviceId) {
        return ResponseEntity.ok(thermostatLogService.getLogsByDevice(deviceId));
    }

    @GetMapping("/{deviceId}/logs/latest")
    public ResponseEntity<ThermostatLogResponse> getLatestLog(@PathVariable Long deviceId) {
        return ResponseEntity.ok(thermostatLogService.getLatestLogByDevice(deviceId));
    }
}
