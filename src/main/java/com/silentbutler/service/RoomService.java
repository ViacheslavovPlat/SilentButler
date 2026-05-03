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

    // ADMIN: оставить как id-based метод
    public RoomResponse createRoom(CreateRoomRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Room room = Room.builder()
                .name(request.getName())
                .user(user)
                .build();

        Room savedRoom = roomRepository.save(room);
        return mapToRoomResponse(savedRoom);
    }

    // USER: вернуть только мои комнаты
    public List<RoomResponse> getRoomsForCurrentUser(String username) {
        User user = getUserByUsername(username);

        return roomRepository.findByUser(user)
                .stream()
                .map(this::mapToRoomResponse)
                .toList();
    }

    // USER: создать комнату только для текущего пользователя
    public RoomResponse createRoomForCurrentUser(CreateRoomRequest request, String username) {
        User user = getUserByUsername(username);

        Room room = Room.builder()
                .name(request.getName())
                .user(user) // request.getUserId() намеренно игнорируется
                .build();

        Room savedRoom = roomRepository.save(room);
        return mapToRoomResponse(savedRoom);
    }

    // ADMIN
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return mapToRoomResponse(room);
    }

    // USER
    public RoomResponse getRoomByIdForCurrentUser(Long roomId, String username) {
        User user = getUserByUsername(username);
        Room room = getOwnedRoom(roomId, user.getId());
        return mapToRoomResponse(room);
    }

    // ADMIN
    public List<RoomResponse> getRoomsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return roomRepository.findByUser(user)
                .stream()
                .map(this::mapToRoomResponse)
                .toList();
    }

    // ADMIN
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        roomRepository.delete(room);
    }

    // USER
    public void deleteRoomForCurrentUser(Long roomId, String username) {
        User user = getUserByUsername(username);
        Room room = getOwnedRoom(roomId, user.getId());
        roomRepository.delete(room);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with username: " + username));
    }

    private Room getOwnedRoom(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        if (!room.getUser().getId().equals(userId)) {
            // сознательно 404, чтобы не раскрывать существование чужой комнаты
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }

        return room;
    }

    private RoomResponse mapToRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .userId(room.getUser().getId())
                .build();
    }
}
