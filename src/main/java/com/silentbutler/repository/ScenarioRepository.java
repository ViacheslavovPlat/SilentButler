package com.silentbutler.repository;

import com.silentbutler.domain.House;
import com.silentbutler.domain.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    List<Scenario> findByHouse(House house);
}