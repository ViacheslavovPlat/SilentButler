package com.silentbutler.repository;

import com.silentbutler.domain.Device;
import com.silentbutler.domain.Event;
import com.silentbutler.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDevice(Device device);
    List<Event> findByUser(User user);
}