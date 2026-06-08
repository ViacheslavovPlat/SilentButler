import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./components/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./components/register/register.component').then(m => m.RegisterComponent) },
  { path: 'dashboard', loadComponent: () => import('./components/dashboard/dashboard.component').then(m => m.DashboardComponent), canActivate: [authGuard] },
  { path: 'rooms', loadComponent: () => import('./components/rooms/rooms.component').then(m => m.RoomsComponent), canActivate: [authGuard] },
  { path: 'devices/:roomId', loadComponent: () => import('./components/devices/devices.component').then(m => m.DevicesComponent), canActivate: [authGuard] },
  { path: '**', redirectTo: 'dashboard' }
];
