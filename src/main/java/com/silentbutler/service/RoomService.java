package com.silentbutler.service;

import com.silentbutler.domain.House;
import com.silentbutler.domain.Room;
import com.silentbutler.dto.CreateRoomRequest;
import com.silentbutler.dto.RoomResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.HouseRepository;
import com.silentbutler.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HouseRepository houseRepository;

    public RoomService(RoomRepository roomRepository, HouseRepository houseRepository) {
        this.roomRepository = roomRepository;
        this.houseRepository = houseRepository;
    }

    public RoomResponse createRoom(CreateRoomRequest request) {
        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));

        Room room = Room.builder()
                .name(request.getName())
                .description(request.getDescription())
                .house(house)
                .createdAt(LocalDateTime.now())
                .build();

        roomRepository.save(room);
        return mapToResponse(room);
    }

    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return mapToResponse(room);
    }

    public List<RoomResponse> getRoomsByHouse(Long houseId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        return roomRepository.findByHouse(house)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        roomRepository.delete(room);
    }

    private RoomResponse mapToResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .description(room.getDescription())
                .houseId(room.getHouse().getId())
                .build();
    }
}