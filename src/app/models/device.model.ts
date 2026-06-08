export interface DeviceResponse {
  id: number;
  name: string;
  categoryName: string;
  roomId: number;
  status: boolean;
  active: boolean;
}

export interface CreateDeviceRequest {
  name: string;
  categoryId: number;
  roomId: number;
  status: boolean;
  active: boolean;
}

export interface DeviceCategoryResponse {
  id: number;
  name: string;
  icon?: string;
  description?: string;
}
