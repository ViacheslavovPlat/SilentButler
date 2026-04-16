package com.silentbutler.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceResponse {
    private Long id;
    private String name;
    private String type;
    private boolean status;
    private Long roomId;
}