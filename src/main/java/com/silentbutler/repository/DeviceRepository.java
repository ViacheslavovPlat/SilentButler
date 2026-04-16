package com.silentbutler.repository;

import com.silentbutler.domain.Device;
import com.silentbutler.domain.Room;
import com.silentbutler.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByRoom(Room room);
    List<Device> findByRoomUser(User user); // devices across all rooms of a user
}