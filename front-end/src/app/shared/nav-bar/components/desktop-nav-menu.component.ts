// components/desktop-nav-menu/desktop-nav-menu.component.ts
import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavPage } from '../nav-bar.component';

@Component({
  selector: 'app-desktop-nav-menu',
  imports: [RouterModule],
  template: `
    <nav>
      <ul class="flex items-center space-x-6">
        @for (item of displayedPages; track $index) {
          <li>
            <a
              [routerLink]="item.url"
              class="text-gray-200 hover:text-gray-50 font-medium text-sm uppercase tracking-wider transition-all duration-200 hover:scale-105 relative group whitespace-nowrap"
            >
              {{ item.page }}
              <span class="absolute -bottom-1 left-0 w-0 h-0.5 bg-white transition-all duration-300 group-hover:w-full"></span>
            </a>
          </li>
        }

        <!-- Dropdown for additional pages -->
        @if (hasMorePages) {
          <li class="relative group">
            <button class="text-gray-200 hover:text-gray-50 font-medium text-sm uppercase tracking-wider transition-all duration-200 flex items-center space-x-1">
              <span>More</span>
              <svg class="w-4 h-4 transition-transform duration-300 group-hover:rotate-180" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
              </svg>
              <span class="absolute -bottom-1 left-0 w-0 h-0.5 bg-white transition-all duration-300 group-hover:w-full"></span>
            </button>

            <div class="absolute right-0 mt-2 w-40 bg-emerald-950 rounded-lg shadow-xl border border-slate-900/70 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 transform translate-y-2 group-hover:translate-y-0 z-50">
              <ul class="py-2">
                @for (item of additionalPages; track $index) {
                  <li>
                    <a
                      [routerLink]="item.url"
                      class="block px-4 py-3 text-gray-100 hover:bg-emerald-900/40 hover:text-blue-600 transition-all duration-200 text-sm font-medium"
                    >
                      {{ item.page }}
                    </a>
                  </li>
                }
              </ul>
            </div>
          </li>
        }
      </ul>
    </nav>
  `
})
export class DesktopNavMenuComponent {
  @Input() navPages: NavPage[] = [];

  get displayedPages(): NavPage[] {
    return this.navPages.slice(0, 4);
  }

  get additionalPages(): NavPage[] {
    return this.navPages.slice(4);
  }

  get hasMorePages(): boolean {
    return this.navPages.length > 4;
  }
}