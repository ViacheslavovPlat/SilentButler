package com.silentbutler.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceCategoryResponse {
    private Long id;
    private String name;
    private String icon;
    private String description;
}