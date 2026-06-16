package com.silentbutler.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThermostatLogResponse {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private Double temperature;
    private Double targetTemperature;
    private Double humidity;
    private String mode;
    private String status;
    private Integer batteryLevel;
    private LocalDateTime createdAt;
}
