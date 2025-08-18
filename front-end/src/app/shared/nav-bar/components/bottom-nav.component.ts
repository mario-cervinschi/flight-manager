import { Component, Input, Output, EventEmitter } from '@angular/core';
import { UserMenuComponent } from './user-menu.component';

@Component({
  selector: 'app-bottom-nav',
  imports: [UserMenuComponent],
  template: `
    <!-- Bottom nav bar - screen < sm-->
    <div class="sm:hidden fixed bottom-0 left-0 w-full h-20 bg-gradient-to-r to-custom-nav-secondary via-custom-nav-via from-custom-nav-secondary shadow-lg z-50">
      <div class="flex items-center justify-around h-full px-4">
        
        <!-- Profile Button -->
        <app-user-menu mode="button"></app-user-menu>

        <!-- Main Flights Button -->
        <button class="flex flex-col items-center justify-center w-20 h-20 bg-white text-emerald-900 rounded-full shadow-2xl mb-12 hover:bg-gray-50 transition-all duration-200 transform hover:scale-105">
          <svg
            class="w-8 h-8"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"
            ></path>
          </svg>
          <span class="text-xs mt-1 font-semibold">Flights</span>
        </button>

        <!-- Menu Button -->
        <button
          class="flex flex-col items-center justify-center w-12 h-12 text-white hover:bg-white/10 rounded-lg transition-colors duration-200"
          (click)="onToggleMenu()"
          [attr.aria-expanded]="isMobileMenuOpen"
          aria-label="Toggle navigation menu"
        >
          <svg
            class="w-6 h-6 transition-transform duration-300"
            [class.rotate-90]="isMobileMenuOpen"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              [attr.d]="hamburgerPath"
            ></path>
          </svg>
          <span class="text-sm mt-1">Menu</span>
        </button>
      </div>
    </div>
  `
})
export class BottomNavComponent {
  @Input() isMobileMenuOpen = false;
  @Output() toggleMenu = new EventEmitter<void>();

  get hamburgerPath(): string {
    return this.isMobileMenuOpen
      ? 'M6 18L18 6M6 6l12 12'
      : 'M4 6h16M4 12h16M4 18h16';
  }

  onToggleMenu() {
    this.toggleMenu.emit();
  }
}