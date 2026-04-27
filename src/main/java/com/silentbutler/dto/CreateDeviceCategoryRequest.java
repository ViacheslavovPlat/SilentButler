package com.silentbutler.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDeviceCategoryRequest {
    @NotBlank(message = "Category name must not be blank")
    private String name;

    private String icon;
    private String description;
}