package com.silentbutler.repository;

import com.silentbutler.domain.Room;
import com.silentbutler.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByUser(User user);
}