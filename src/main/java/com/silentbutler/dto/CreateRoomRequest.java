package com.silentbutler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoomRequest {
    @NotBlank(message = "Room name must not be blank")
    private String name;

    @NotNull(message = "User ID must not be null")
    private Long userId;
}