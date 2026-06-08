import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserResponse, CreateUserRequest, UpdateUserRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly API = '/api/users';

  constructor(private http: HttpClient) {}

  getAll(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.API);
  }

  getById(id: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.API}/${id}`);
  }

  getMe(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.API}/me`);
  }

  create(request: CreateUserRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(this.API, request);
  }

  update(id: number, request: UpdateUserRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.API}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}
