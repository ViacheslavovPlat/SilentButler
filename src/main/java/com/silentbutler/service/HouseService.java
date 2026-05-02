package com.silentbutler.service;

import com.silentbutler.domain.House;
import com.silentbutler.domain.User;
import com.silentbutler.dto.CreateHouseRequest;
import com.silentbutler.dto.HouseResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.HouseRepository;
import com.silentbutler.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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
        User owner = resolveCurrentUser();

        House house = House.builder()
                .name(request.getName())
                .address(request.getAddress())
                .user(owner)
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(houseRepository.save(house));
    }

    public HouseResponse getHouseById(Long id) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        assertOwnership(house);
        return mapToResponse(house);
    }

    public List<HouseResponse> getMyHouses() {
        User owner = resolveCurrentUser();
        return houseRepository.findByUser(owner)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteHouse(Long id) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        assertOwnership(house);
        houseRepository.delete(house);
    }

    //  Helpers 

    private User resolveCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void assertOwnership(House house) {
        User currentUser = resolveCurrentUser();
        if (!house.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have access to this house");
        }
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