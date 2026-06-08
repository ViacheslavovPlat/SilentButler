import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DeviceResponse, CreateDeviceRequest, DeviceCategoryResponse } from '../models';

@Injectable({ providedIn: 'root' })
export class DeviceService {
  private readonly API = '/api/devices';
  private readonly CATEGORIES_API = '/api/device-categories';

  constructor(private http: HttpClient) {}

  getByRoom(roomId: number): Observable<DeviceResponse[]> {
    return this.http.get<DeviceResponse[]>(`${this.API}/room/${roomId}`);
  }

  getById(id: number): Observable<DeviceResponse> {
    return this.http.get<DeviceResponse>(`${this.API}/${id}`);
  }

  create(request: CreateDeviceRequest): Observable<DeviceResponse> {
    return this.http.post<DeviceResponse>(this.API, request);
  }

  toggle(id: number): Observable<DeviceResponse> {
    return this.http.patch<DeviceResponse>(`${this.API}/${id}/toggle`, {});
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }

  getCategories(): Observable<DeviceCategoryResponse[]> {
    return this.http.get<DeviceCategoryResponse[]>(this.CATEGORIES_API);
  }
}
