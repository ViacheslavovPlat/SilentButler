package com.silentbutler.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateDeviceRequest {
    private String name;
    private String type;
    private boolean status;
    private Long roomId; // which room this device belongs to
}