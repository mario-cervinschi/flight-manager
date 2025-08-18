import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService, User } from '../../auth.service';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-menu',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  template: `
    <ng-container *ngIf="mode === 'dropdown'">
      <div class="relative">
        <!-- Login Button (when not logged in) -->
        <button
          *ngIf="!isLoggedIn"
          (click)="openLoginModal()"
          class="bg-custom-nav-via/50 border-custom-secondary-background/50 border hover:bg-custom-nav-primary/70 hover:border-custom-secondary-background/30 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
        >
          Login
        </button>

        <!-- User Account Menu (when logged in) -->
        <div *ngIf="isLoggedIn" class="relative">
          <button
            (click)="toggleDropdown()"
            class="flex items-center space-x-2 bg-transparent hover:bg-gray-500/10 px-3 py-2 rounded-md text-sm font-medium transition-colors"
          >
            <div
              class="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center text-white text-xs font-bold"
            >
              {{ getInitials() }}
            </div>
            <span class="hidden md:block text-white">{{
              currentUser?.name
            }}</span>
            <svg class="w-4 h-4" fill="none" stroke="white" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M19 9l-7 7-7-7"
              ></path>
            </svg>
          </button>

          <!-- Dropdown Menu -->
          <div
            *ngIf="isDropdownOpen"
            class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50 border"
          >
            <a
              routerLink="/profile"
              (click)="closeDropdown()"
              class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
            >
              My Profile
            </a>
            <a
              routerLink="/bookings"
              (click)="closeDropdown()"
              class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
            >
              My Bookings
            </a>
            <a
              routerLink="/settings"
              (click)="closeDropdown()"
              class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
            >
              Settings
            </a>
            <hr class="my-1" />
            <button
              (click)="logout()"
              class="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-gray-100"
            >
              Logout
            </button>
          </div>
        </div>

        <div
          *ngIf="showLoginModal"
          class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
          (click)="closeLoginModal()"
        >
          <div
            class="bg-white rounded-lg p-6 w-full max-w-md mx-4"
            (click)="$event.stopPropagation()"
          >
            <h2 class="text-xl font-bold mb-4">Login</h2>
            <form (ngSubmit)="onLogin()">
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-1"
                  >Email</label
                >
                <input
                  type="email"
                  [(ngModel)]="loginForm.email"
                  name="email"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500"
                />
              </div>
              <div class="mb-6">
                <label class="block text-sm font-medium text-gray-700 mb-1"
                  >Password</label
                >
                <input
                  type="password"
                  [(ngModel)]="loginForm.password"
                  name="password"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500"
                />
              </div>
              <div class="flex space-x-3">
                <button
                  type="button"
                  (click)="closeLoginModal()"
                  class="flex-1 bg-gray-300 hover:bg-gray-400 text-gray-700 py-2 px-4 rounded-md font-medium"
                >
                  Cancel</button
                ><button
                  type="submit"
                  [disabled]="isLoggingIn"
                  class="flex-1 bg-emerald-600 hover:bg-emerald-700 disabled:bg-emerald-700 text-white py-2 px-4 rounded-md font-medium"
                >
                  {{ isLoggingIn ? 'Logging in...' : 'Login' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </ng-container>
    <ng-container *ngIf="mode === 'button'">
      <button
        class="flex flex-col items-center justify-center w-12 h-16 text-white hover:bg-white/10 rounded-lg transition-colors duration-200"
        (click)="onButtonClick()"
      >
        @if(isLoggedIn){
        <div
          class="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center text-white text-xs font-bold"
        >
          {{ getInitials() }}
        </div>
        } @else {
        <svg
          v-else
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
          class="w-6 h-6"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z"
          />
        </svg>

        }
        <span class="text-sm mt-1"> Profile </span>
      </button>

      <div
        *ngIf="showLoginModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
        (click)="closeLoginModal()"
      >
        <div
          class="bg-white rounded-lg p-6 w-full max-w-md mx-4"
          (click)="$event.stopPropagation()"
        >
          <h2 class="text-xl font-bold mb-4">Login</h2>
          <form (ngSubmit)="onLogin()">
            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700 mb-1"
                >Email</label
              >
              <input
                type="email"
                [(ngModel)]="loginForm.email"
                name="email"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>
            <div class="mb-6">
              <label class="block text-sm font-medium text-gray-700 mb-1"
                >Password</label
              >
              <input
                type="password"
                [(ngModel)]="loginForm.password"
                name="password"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>
            <div class="flex space-x-3">
              <button
                type="button"
                (click)="closeLoginModal()"
                class="flex-1 bg-gray-300 hover:bg-gray-400 text-gray-700 py-2 px-4 rounded-md font-medium"
              >
                Cancel</button
              ><button
                type="submit"
                [disabled]="isLoggingIn"
                class="flex-1 bg-emerald-600 hover:bg-emerald-700 disabled:bg-emerald-700 text-white py-2 px-4 rounded-md font-medium"
              >
                {{ isLoggingIn ? 'Logging in...' : 'Login' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </ng-container>
  `,
})
export class UserMenuComponent implements OnInit, OnDestroy {
  @Input() mode: 'dropdown' | 'button' = 'dropdown';

  currentUser: User | null = null;
  isLoggedIn = false;
  isDropdownOpen = false;
  showLoginModal = false;
  isLoggingIn = false;

  loginForm = {
    email: '',
    password: '',
  };

  private subscription: Subscription = new Subscription();

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.subscription.add(
      this.authService.currentUser$.subscribe((user) => {
        this.currentUser = user;
        this.isLoggedIn = !!user;
      })
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  getInitials(): string {
    if (!this.currentUser?.name) return 'U';
    return this.currentUser.name
      .split(' ')
      .map((n) => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  }

  onButtonClick() {
    if (this.isLoggedIn) {
      this.router.navigate(['/profile']);
    } else {
      this.openLoginModal();
    }
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  closeDropdown() {
    this.isDropdownOpen = false;
  }

  openLoginModal() {
    this.showLoginModal = true;
  }

  closeLoginModal() {
    this.showLoginModal = false;
    this.loginForm = { email: '', password: '' };
  }

  async onLogin() {
    if (!this.loginForm.email || !this.loginForm.password) return;

    this.isLoggingIn = true;
    try {
      await this.authService.login(
        this.loginForm.email,
        this.loginForm.password
      );
      this.closeLoginModal();
    } catch (error) {
      console.error('Login failed:', error);
    } finally {
      this.isLoggingIn = false;
    }
  }

  logout() {
    this.authService.logout();
    this.closeDropdown();
  }
}
