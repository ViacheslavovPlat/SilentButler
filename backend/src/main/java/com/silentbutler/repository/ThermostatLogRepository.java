package com.silentbutler.repository;

import com.silentbutler.domain.Device;
import com.silentbutler.domain.ThermostatLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThermostatLogRepository extends JpaRepository<ThermostatLog, Long> {
    List<ThermostatLog> findByDeviceOrderByCreatedAtDesc(Device device);
    Optional<ThermostatLog> findFirstByDeviceOrderByCreatedAtDesc(Device device);
}
