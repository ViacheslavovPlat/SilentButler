package com.silentbutler.service;

import com.silentbutler.domain.House;
import com.silentbutler.domain.Scenario;
import com.silentbutler.dto.CreateScenarioRequest;
import com.silentbutler.dto.ScenarioResponse;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.HouseRepository;
import com.silentbutler.repository.ScenarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final HouseRepository houseRepository;

    public ScenarioService(ScenarioRepository scenarioRepository,
                           HouseRepository houseRepository) {
        this.scenarioRepository = scenarioRepository;
        this.houseRepository = houseRepository;
    }

    public ScenarioResponse createScenario(CreateScenarioRequest request) {
        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));

        Scenario scenario = Scenario.builder()
                .name(request.getName())
                .description(request.getDescription())
                .house(house)
                .active(request.isActive())
                .createdAt(LocalDateTime.now())
                .build();

        scenarioRepository.save(scenario);
        return mapToResponse(scenario);
    }

    public ScenarioResponse getScenarioById(Long id) {
        Scenario scenario = scenarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scenario not found"));
        return mapToResponse(scenario);
    }

    public List<ScenarioResponse> getScenariosByHouse(Long houseId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ResourceNotFoundException("House not found"));
        return scenarioRepository.findByHouse(house)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteScenario(Long id) {
        Scenario scenario = scenarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scenario not found"));
        scenarioRepository.delete(scenario);
    }

    private ScenarioResponse mapToResponse(Scenario scenario) {
        return ScenarioResponse.builder()
                .id(scenario.getId())
                .name(scenario.getName())
                .description(scenario.getDescription())
                .houseId(scenario.getHouse().getId())
                .active(scenario.isActive())
                .build();
    }
}