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

    @NotNull(message = "Category ID must not be null")
    private Long categoryId;

    @NotNull(message = "Room ID must not be null")
    private Long roomId;

    private boolean status;
    private boolean active;
}