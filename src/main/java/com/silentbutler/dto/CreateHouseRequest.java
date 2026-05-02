package com.silentbutler.dto;

import jakarta.validation.constraints.NotBlank;
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

    // userId removed — the owner is resolved from the JWT token, not supplied by the client
}