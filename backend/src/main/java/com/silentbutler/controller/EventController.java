package com.silentbutler.controller;

import com.silentbutler.dto.EventResponse;
import com.silentbutler.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<EventResponse>> getEventsByDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(eventService.getEventsByDevice(deviceId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventResponse>> getEventsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(eventService.getEventsByUser(userId));
    }
}