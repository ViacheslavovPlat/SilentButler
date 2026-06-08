import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { RoomService } from '../../services/room.service';
import { HouseService } from '../../services/house.service';
import { UserService } from '../../services/user.service';
import { RoomResponse, CreateRoomRequest, UserResponse } from '../../models';

@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, NavbarComponent],
  templateUrl: './rooms.component.html'
})
export class RoomsComponent implements OnInit {
  rooms: RoomResponse[] = [];
  user: UserResponse | null = null;
  currentHouseId: number | null = null;
  currentHouseName = '';
  newRoomName = '';
  newHouseName = '';
  newHouseAddress = '';
  noHouse = false;
  creatingHouse = false;
  loading = true;
  error = '';

  constructor(
    private roomService: RoomService,
    private houseService: HouseService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.userService.getMe().subscribe({
      next: (u) => {
        this.user = u;
        this.loadHouseAndRooms();
      },
      error: () => {
        this.error = 'Failed to load user';
        this.loading = false;
      }
    });
  }

  loadHouseAndRooms(): void {
    this.houseService.getMyHouses().subscribe({
      next: (houses) => {
        if (houses.length > 0) {
          this.noHouse = false;
          this.currentHouseId = houses[0].id;
          this.currentHouseName = houses[0].name;
          this.loadRooms();
        } else {
          this.noHouse = true;
          this.rooms = [];
          this.loading = false;
        }
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to load house';
        this.loading = false;
      }
    });
  }

  createHouse(): void {
    if (!this.newHouseName.trim() || !this.newHouseAddress.trim()) return;
    this.creatingHouse = true;
    this.error = '';
    this.houseService.create({
      name: this.newHouseName.trim(),
      address: this.newHouseAddress.trim()
    }).subscribe({
      next: (house) => {
        this.noHouse = false;
        this.currentHouseId = house.id;
        this.currentHouseName = house.name;
        this.newHouseName = '';
        this.newHouseAddress = '';
        this.creatingHouse = false;
        this.loadRooms();
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to create house';
        this.creatingHouse = false;
      }
    });
  }

  loadRooms(): void {
    if (!this.currentHouseId) {
      this.loading = false;
      return;
    }
    this.roomService.getByHouse(this.currentHouseId).subscribe({
      next: (rooms) => {
        this.rooms = rooms;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to load rooms';
        this.loading = false;
      }
    });
  }

  createRoom(): void {
    if (!this.newRoomName.trim() || !this.currentHouseId) return;
    const request: CreateRoomRequest = {
      name: this.newRoomName.trim(),
      description: '',
      houseId: this.currentHouseId
    };
    this.roomService.create(request).subscribe({
      next: () => {
        this.newRoomName = '';
        this.loadRooms();
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to create room';
      }
    });
  }

  deleteRoom(id: number): void {
    if (!confirm('Delete this room?')) return;
    this.roomService.delete(id).subscribe({
      next: () => this.loadRooms(),
      error: (err) => {
        this.error = err.error?.message || 'Failed to delete room';
      }
    });
  }
}
