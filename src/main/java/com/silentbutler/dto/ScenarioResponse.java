package com.silentbutler.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScenarioResponse {
    private Long id;
    private String name;
    private String description;
    private Long houseId;
    private boolean active;
}