import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoomResponse, CreateRoomRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class RoomService {
  private readonly API = '/api/rooms';

  constructor(private http: HttpClient) {}

  getByHouse(houseId: number): Observable<RoomResponse[]> {
    return this.http.get<RoomResponse[]>(`${this.API}/house/${houseId}`);
  }

  getById(id: number): Observable<RoomResponse> {
    return this.http.get<RoomResponse>(`${this.API}/${id}`);
  }

  create(request: CreateRoomRequest): Observable<RoomResponse> {
    return this.http.post<RoomResponse>(this.API, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}
