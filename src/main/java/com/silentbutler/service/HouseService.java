package com.silentbutler.service;

import com.silentbutler.domain.House;
import com.silentbutler.domain.User;
import com.silentbutler.dto.CreateHouseRequest;
import com.silentbutler.dto.HouseResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.HouseRepository;
import com.silentbutler.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    public HouseService(HouseRepository houseRepository, UserRepository userRepository) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }

    public HouseResponse createHouse(CreateHouseRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        House house = House.builder()
                .name(request.getName())
                .address(request.getAddress())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        houseRepository.save(house);
        return mapToResponse(house);
    }

    public HouseResponse getHouseById(Long id) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        return mapToResponse(house);
    }

    public List<HouseResponse> getHousesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return houseRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteHouse(Long id) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        houseRepository.delete(house);
    }

    private HouseResponse mapToResponse(House house) {
        return HouseResponse.builder()
                .id(house.getId())
                .name(house.getName())
                .address(house.getAddress())
                .userId(house.getUser().getId())
                .build();
    }
}