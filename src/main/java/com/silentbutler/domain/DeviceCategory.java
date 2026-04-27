package com.silentbutler.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "device_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g. "LIGHT", "THERMOSTAT", "LOCK"

    private String icon;

    private String description;
}