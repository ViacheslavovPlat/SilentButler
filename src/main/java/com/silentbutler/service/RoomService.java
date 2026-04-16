package com.silentbutler.service;

import com.silentbutler.domain.Room;
import com.silentbutler.domain.User;
import com.silentbutler.dto.CreateRoomRequest;
import com.silentbutler.dto.RoomResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.RoomRepository;
import com.silentbutler.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public RoomResponse createRoom(CreateRoomRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Room room = Room.builder()
                .name(request.getName())
                .user(user)
                .build();

        roomRepository.save(room);
        return mapToRoomResponse(room);
    }

    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return mapToRoomResponse(room);
    }

    public List<RoomResponse> getRoomsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return roomRepository.findByUser(user)
                .stream()
                .map(this::mapToRoomResponse)
                .toList();
    }

    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        roomRepository.delete(room);
    }

    private RoomResponse mapToRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .userId(room.getUser().getId())
                .build();
    }
}