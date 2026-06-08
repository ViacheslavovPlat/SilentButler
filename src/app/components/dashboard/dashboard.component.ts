import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { UserService } from '../../services/user.service';
import { RoomService } from '../../services/room.service';
import { HouseService } from '../../services/house.service';
import { DeviceService } from '../../services/device.service';
import { forkJoin } from 'rxjs';
import { UserResponse, RoomResponse } from '../../models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  user: UserResponse | null = null;
  rooms: RoomResponse[] = [];
  totalDevices = 0;
  loading = true;
  error = '';

  constructor(
    private userService: UserService,
    private roomService: RoomService,
    private houseService: HouseService,
    private deviceService: DeviceService
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
          this.roomService.getByHouse(houses[0].id).subscribe({
            next: (rooms) => {
              this.rooms = rooms;
              this.loadDeviceCount(rooms);
            },
            error: () => {
              this.loading = false;
            }
          });
        } else {
          this.rooms = [];
          this.totalDevices = 0;
          this.loading = false;
        }
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  private loadDeviceCount(rooms: RoomResponse[]): void {
    if (rooms.length === 0) {
      this.totalDevices = 0;
      this.loading = false;
      return;
    }
    const requests = rooms.map(r => this.deviceService.getByRoom(r.id));
    forkJoin(requests).subscribe({
      next: (results) => {
        this.totalDevices = results.reduce((sum, arr) => sum + arr.length, 0);
        this.loading = false;
      },
      error: () => {
        this.totalDevices = 0;
        this.loading = false;
      }
    });
  }

  get deviceCount(): number {
    return this.totalDevices;
  }
}
