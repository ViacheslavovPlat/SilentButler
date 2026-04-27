package com.silentbutler.service;

import com.silentbutler.domain.Device;
import com.silentbutler.domain.Event;
import com.silentbutler.domain.User;
import com.silentbutler.dto.EventResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.DeviceRepository;
import com.silentbutler.repository.EventRepository;
import com.silentbutler.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository,
                        DeviceRepository deviceRepository,
                        UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    public List<EventResponse> getEventsByDevice(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        return eventRepository.findByDevice(device)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<EventResponse> getEventsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return eventRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .deviceId(event.getDevice().getId())
                .deviceName(event.getDevice().getName())
                .eventType(event.getEventType())
                .description(event.getDescription())
                .oldState(event.getOldState())
                .newState(event.getNewState())
                .timestamp(event.getTimestamp())
                .build();
    }
}