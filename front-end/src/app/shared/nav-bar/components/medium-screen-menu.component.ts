import { Component, Input, Output, EventEmitter } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavPage } from '../nav-bar.component';

@Component({
  selector: 'app-medium-screen-menu',
  imports: [RouterModule],
  template: `
    <div
      class="transition-all duration-200 ease-in-out"
      [class]="menuClasses"
    >
      <div class="bg-gradient-to-b from-slate-100/100 via-slate-50/95 to-custom-card-background/60 backdrop-blur-sm border-t border-white/10">
        <nav class="h-screen overflow-y-auto">
          <ul class="space-y-2 py-2">
            @for (item of navPages; track $index) {
              <li>
                <a
                  [routerLink]="item.url"
                  (click)="onCloseMenu()"
                  class="block py-3 px-4 text-emerald-900 hover:bg-custom-hover-link/40 rounded-lg transition-all duration-200 text-lg font-medium text-center ml-20 mr-20"
                >
                  {{ item.page }}
                </a>
              </li>
            }
          </ul>
        </nav>
      </div>
    </div>
  `
})
export class MediumScreenMenuComponent {
  @Input() navPages: NavPage[] = [];
  @Input() isMobileMenuOpen = false;
  @Output() closeMenu = new EventEmitter<void>();

  get menuClasses(): string {
    return this.isMobileMenuOpen
      ? 'max-h-96 opacity-100'
      : 'max-h-0 opacity-0 overflow-hidden';
  }

  onCloseMenu() {
    this.closeMenu.emit();
  }
}