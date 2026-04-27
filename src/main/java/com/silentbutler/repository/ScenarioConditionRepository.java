package com.silentbutler.repository;

import com.silentbutler.domain.Scenario;
import com.silentbutler.domain.ScenarioCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, Long> {
    List<ScenarioCondition> findByScenario(Scenario scenario);
}