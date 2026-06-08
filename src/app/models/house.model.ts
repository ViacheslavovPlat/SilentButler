export interface HouseResponse {
  id: number;
  name: string;
  address?: string;
  userId: number;
}

export interface CreateHouseRequest {
  name: string;
  address: string;
}
