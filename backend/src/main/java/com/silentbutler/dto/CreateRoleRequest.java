package com.silentbutler.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoleRequest {
    @NotBlank(message = "Role name must not be blank")
    private String name;

    private String description;
}