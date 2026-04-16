package com.silentbutler.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoomResponse {
    private Long id;
    private String name;
    private Long userId;
}