package com.silentbutler.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private Long userId;
    private String eventType;
    private String description;
    private String oldState;
    private String newState;
    private LocalDateTime timestamp;
}