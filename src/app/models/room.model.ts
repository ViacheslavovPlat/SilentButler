export interface RoomResponse {
  id: number;
  name: string;
  description?: string;
  houseId: number;
}

export interface CreateRoomRequest {
  name: string;
  description?: string;
  houseId: number;
}
