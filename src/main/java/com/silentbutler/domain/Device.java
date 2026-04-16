package com.silentbutler.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g. "Smart Bulb"

    @Column(nullable = false)
    private String type; // e.g. "LIGHT", "THERMOSTAT", "LOCK"

    @Column(nullable = false)
    private boolean status; // true = on, false = off

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}