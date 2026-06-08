package com.silentbutler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateScenarioRequest {
    @NotBlank(message = "Scenario name must not be blank")
    private String name;

    private String description;

    @NotNull(message = "House ID must not be null")
    private Long houseId;

    private boolean active;
}