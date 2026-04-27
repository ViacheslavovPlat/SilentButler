package com.silentbutler.repository;

import com.silentbutler.domain.Scenario;
import com.silentbutler.domain.ScenarioAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, Long> {
    List<ScenarioAction> findByScenario(Scenario scenario);
}