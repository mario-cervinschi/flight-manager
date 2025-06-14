import { Component, Input, Output, EventEmitter } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavPage } from '../nav-bar.component';

@Component({
  selector: 'app-mobile-menu-overlay',
  imports: [RouterModule],
  template: `
    <div class="sm:hidden fixed inset-0 z-40 pointer-events-none">
      <!-- Backdrop -->
      <div
        class="absolute inset-0 bg-black/50 transition-opacity duration-300 pointer-events-auto"
        [class.opacity-0]="!isMobileMenuOpen"
        [class.opacity-100]="isMobileMenuOpen"
        [class.invisible]="!isMobileMenuOpen"
        (click)="onCloseMenu()"
      ></div>

      <!-- Menu Panel -->
      <div
        class="absolute bottom-16 left-0 right-0 bg-slate-50 rounded-t-xl shadow-2xl transition-transform duration-300 transform pointer-events-auto"
        [class.translate-y-full]="!isMobileMenuOpen"
        [class.translate-y-0]="isMobileMenuOpen"
      >
        <div class="px-4 py-6 max-h-96 overflow-y-auto">
          <h3 class="text-lg font-semibold text-emerald-950 mb-4 text-center">
            Navigation
          </h3>
          <nav>
            <ul class="space-y-3">
              @for (item of navPages; track $index) {
                <li>
                  <a
                    [routerLink]="item.url"
                    (click)="onCloseMenu()"
                    class="block py-3 px-4 text-emerald-900 hover:bg-custom-hover-link/40 rounded-lg transition-all duration-200 text-center font-medium"
                  >
                    {{ item.page }}
                  </a>
                </li>
              }
            </ul>
          </nav>
        </div>
      </div>
    </div>
  `
})
export class MobileMenuOverlayComponent {
  @Input() navPages: NavPage[] = [];
  @Input() isMobileMenuOpen = false;
  @Output() closeMenu = new EventEmitter<void>();

  onCloseMenu() {
    this.closeMenu.emit();
  }
}