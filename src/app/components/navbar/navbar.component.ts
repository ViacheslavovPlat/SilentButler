import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { UserResponse } from '../../models';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.component.html'
})
export class NavbarComponent {
  user: UserResponse | null = null;

  constructor(
    public auth: AuthService,
    private userService: UserService,
    private router: Router
  ) {
    if (this.auth.isAuthenticated()) {
      this.userService.getMe().subscribe(u => this.user = u);
    }
  }

  logout(): void {
    this.auth.logout();
  }
}
