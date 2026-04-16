package com.silentbutler.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateRoomRequest {
    private String name;
    private Long userId; // which user this room belongs to
}