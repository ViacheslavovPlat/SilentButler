package com.silentbutler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDeviceRequest {
    @NotBlank(message = "Device name must not be blank")
    private String name;

    @NotBlank(message = "Device type must not be blank")
    private String type;

    @NotNull(message = "Room ID must not be null")
    private Long roomId;

    private boolean status;
}