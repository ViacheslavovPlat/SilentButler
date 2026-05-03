package com.silentbutler.controller;

import com.silentbutler.dto.CreateRoomRequest;
import com.silentbutler.dto.RoomResponse;
import com.silentbutler.service.RoomService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService)
    {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request,
                                                   Authentication authentication) {
        return ResponseEntity.ok(roomService.createRoomForCurrentUser(request,authentication.getName()));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RoomResponse>> getMyRooms(Authentication authentication){
        return ResponseEntity.ok(roomService.getRoomsForCurrentUser(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id,
                                                    Authentication authentication) {
        return ResponseEntity.ok(roomService.getRoomByIdForCurrentUser(id, authentication.getName()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoomResponse>> getRoomsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(roomService.getRoomsByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id,
                                           Authentication authentication) {
        roomService.deleteRoomForCurrentUser(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}