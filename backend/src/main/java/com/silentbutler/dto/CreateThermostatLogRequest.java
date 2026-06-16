package com.silentbutler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateThermostatLogRequest {
    @NotNull(message = "Temperature must not be null")
    private Double temperature;

    private Double targetTemperature;

    private Double humidity;

    private String mode;

    private String status;

    private Integer batteryLevel;
}
