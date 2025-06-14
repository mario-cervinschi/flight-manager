import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-mobile-menu-button',
  imports: [],
  template: `
    <button
      class="text-gray-100 p-2 rounded-md hover:bg-white/10 hover:text-gray-50/90 duration-200"
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
    </button>
  `
})
export class MobileMenuButtonComponent {
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