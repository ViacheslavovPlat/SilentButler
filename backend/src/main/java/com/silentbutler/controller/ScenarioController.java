package com.silentbutler.controller;

import com.silentbutler.dto.CreateScenarioRequest;
import com.silentbutler.dto.ScenarioResponse;
import com.silentbutler.service.ScenarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenarios")
public class ScenarioController {

    private final ScenarioService scenarioService;

    public ScenarioController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @PostMapping
    public ResponseEntity<ScenarioResponse> createScenario(
            @Valid @RequestBody CreateScenarioRequest request) {
        return ResponseEntity.ok(scenarioService.createScenario(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScenarioResponse> getScenarioById(@PathVariable Long id) {
        return ResponseEntity.ok(scenarioService.getScenarioById(id));
    }

    @GetMapping("/house/{houseId}")
    public ResponseEntity<List<ScenarioResponse>> getScenariosByHouse(@PathVariable Long houseId) {
        return ResponseEntity.ok(scenarioService.getScenariosByHouse(houseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScenario(@PathVariable Long id) {
        scenarioService.deleteScenario(id);
        return ResponseEntity.noContent().build();
    }
}