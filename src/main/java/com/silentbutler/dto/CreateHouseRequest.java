package com.silentbutler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateHouseRequest {
    @NotBlank(message = "House name must not be blank")
    private String name;

    @NotBlank(message = "House address must not be blank")
    private String address;

    @NotNull(message = "User ID must not be null")
    private Long userId;
}