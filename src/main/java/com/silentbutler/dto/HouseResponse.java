package com.silentbutler.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseResponse {
    private Long id;
    private String name;
    private String address;
    private Long userId;
}