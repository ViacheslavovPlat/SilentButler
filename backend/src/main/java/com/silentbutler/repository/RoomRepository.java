package com.silentbutler.repository;

import com.silentbutler.domain.House;
import com.silentbutler.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHouse(House house);
}