package com.silentbutler.service;

import com.silentbutler.domain.House;
import com.silentbutler.domain.Room;
import com.silentbutler.domain.User;
import com.silentbutler.dto.CreateRoomRequest;
import com.silentbutler.dto.RoomResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.HouseRepository;
import com.silentbutler.repository.RoomRepository;
import com.silentbutler.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    public RoomService(RoomRepository roomRepository,
                       HouseRepository houseRepository,
                       UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }

    public RoomResponse createRoom(CreateRoomRequest request) {
        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        assertHouseOwnership(house);

        Room room = Room.builder()
                .name(request.getName())
                .description(request.getDescription())
                .house(house)
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(roomRepository.save(room));
    }

    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        assertHouseOwnership(room.getHouse());
        return mapToResponse(room);
    }

    public List<RoomResponse> getRoomsByHouse(Long houseId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        assertHouseOwnership(house);

        return roomRepository.findByHouse(house)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        assertHouseOwnership(room.getHouse());
        roomRepository.delete(room);
    }

    //  Helpers 

    private User resolveCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void assertHouseOwnership(House house) {
        User currentUser = resolveCurrentUser();
        if (!house.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have access to this house");
        }
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