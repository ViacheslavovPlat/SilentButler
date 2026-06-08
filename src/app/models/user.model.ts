export interface UserResponse {
  id: number;
  username: string;
  email: string;
  role: string;
}

export interface CreateUserRequest {
  username: string;
  email: string;
  password: string;
  role: string;
}

export interface UpdateUserRequest {
  username: string;
  email: string;
  role: string;
}
