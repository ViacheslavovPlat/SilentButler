package com.silentbutler.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceResponse {
    private Long id;
    private String name;
    private String categoryName;
    private Long roomId;
    private boolean status;
    private boolean active;
}