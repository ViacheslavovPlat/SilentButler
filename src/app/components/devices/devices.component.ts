import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { DeviceService } from '../../services/device.service';
import { RoomService } from '../../services/room.service';
import { DeviceResponse, CreateDeviceRequest, RoomResponse, DeviceCategoryResponse } from '../../models';

@Component({
  selector: 'app-devices',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, NavbarComponent],
  templateUrl: './devices.component.html'
})
export class DevicesComponent implements OnInit {
  roomId = 0;
  room: RoomResponse | null = null;
  devices: DeviceResponse[] = [];
  categories: DeviceCategoryResponse[] = [];
  newDevice: CreateDeviceRequest = { name: '', categoryId: 0, roomId: 0, status: false, active: true };
  loading = true;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private deviceService: DeviceService,
    private roomService: RoomService
  ) {}

  ngOnInit(): void {
    this.roomId = Number(this.route.snapshot.paramMap.get('roomId'));
    this.newDevice.roomId = this.roomId;
    this.loadRoom();
    this.loadCategories();
    this.loadDevices();
  }

  loadRoom(): void {
    this.roomService.getById(this.roomId).subscribe({
      next: (r) => this.room = r,
      error: () => this.error = 'Failed to load room'
    });
  }

  loadCategories(): void {
    this.deviceService.getCategories().subscribe({
      next: (cats) => {
        this.categories = cats;
        if (cats.length > 0) {
          this.newDevice.categoryId = cats[0].id;
        }
      },
      error: () => {}
    });
  }

  loadDevices(): void {
    this.deviceService.getByRoom(this.roomId).subscribe({
      next: (devices) => {
        this.devices = devices;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to load devices';
        this.loading = false;
      }
    });
  }

  createDevice(): void {
    if (!this.newDevice.name.trim()) return;
    this.deviceService.create(this.newDevice).subscribe({
      next: () => {
        this.newDevice.name = '';
        this.newDevice.status = false;
        this.loadDevices();
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to create device';
      }
    });
  }

  toggleDevice(id: number): void {
    this.deviceService.toggle(id).subscribe({
      next: () => this.loadDevices(),
      error: (err) => {
        this.error = err.error?.message || 'Failed to toggle device';
      }
    });
  }

  deleteDevice(id: number): void {
    if (!confirm('Delete this device?')) return;
    this.deviceService.delete(id).subscribe({
      next: () => this.loadDevices(),
      error: (err) => {
        this.error = err.error?.message || 'Failed to delete device';
      }
    });
  }

  getCategoryIcon(name: string): string {
    switch (name) {
      case 'LIGHT': return '💡';
      case 'THERMOSTAT': return '🌡️';
      case 'LOCK': return '🔒';
      case 'CAMERA': return '📷';
      case 'SPEAKER': return '🔊';
      case 'BLIND': return '🪟';
      default: return '🔌';
    }
  }
}
