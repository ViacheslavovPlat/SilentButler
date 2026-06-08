import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HouseResponse, CreateHouseRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class HouseService {
  private readonly API = '/api/houses';

  constructor(private http: HttpClient) {}

  getMyHouses(): Observable<HouseResponse[]> {
    return this.http.get<HouseResponse[]>(`${this.API}/mine`);
  }

  getById(id: number): Observable<HouseResponse> {
    return this.http.get<HouseResponse>(`${this.API}/${id}`);
  }

  create(request: CreateHouseRequest): Observable<HouseResponse> {
    return this.http.post<HouseResponse>(this.API, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}
