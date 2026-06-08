package com.silentbutler.controller;

import com.silentbutler.dto.CreateHouseRequest;
import com.silentbutler.dto.HouseResponse;
import com.silentbutler.service.HouseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @PostMapping
    public ResponseEntity<HouseResponse> createHouse(@Valid @RequestBody CreateHouseRequest request) {
        return ResponseEntity.ok(houseService.createHouse(request));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<HouseResponse>> getMyHouses() {
        return ResponseEntity.ok(houseService.getMyHouses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseResponse> getHouseById(@PathVariable Long id) {
        return ResponseEntity.ok(houseService.getHouseById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHouse(@PathVariable Long id) {
        houseService.deleteHouse(id);
        return ResponseEntity.noContent().build();
    }
}