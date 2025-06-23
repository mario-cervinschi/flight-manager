import { Component, Input, Output, EventEmitter } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavPage } from '../nav-bar.component';
import { NavLogoComponent } from './nav-logo.component';
import { DesktopNavMenuComponent } from './desktop-nav-menu.component';
import { MobileMenuButtonComponent } from './mobile-menu-button.component';
import { MediumScreenMenuComponent } from './medium-screen-menu.component';

@Component({
  selector: 'app-top-nav',
  imports: [
    RouterModule, 
    NavLogoComponent, 
    DesktopNavMenuComponent, 
    MobileMenuButtonComponent,
    MediumScreenMenuComponent
  ],
  template: `
    <!-- Top nav bar -->
    <div class="z-50 w-full h-16 bg-gradient-to-r from-custom-nav-secondary via-custom-nav-via to-custom-nav-secondary 
                sm:bg-gradient-to-r sm:from-custom-nav-via sm:to-custom-nav-secondary shadow-lg">
      <div class="flex items-center sm:justify-between justify-center h-full px-6">
        
        <app-nav-logo></app-nav-logo>

        <!-- Large screen navigation -->
        <app-desktop-nav-menu 
          [navPages]="navPages"
          class="hidden lg:block">
        </app-desktop-nav-menu>

        <!-- Burger button for medium screen devices -->
        <app-mobile-menu-button
          [isMobileMenuOpen]="isMobileMenuOpen"
          (toggleMenu)="onToggleMenu()"
          class="hidden sm:block lg:hidden">
        </app-mobile-menu-button>
      </div>

      <!-- Medium screen dropdown menu -->
      <app-medium-screen-menu
        [navPages]="navPages"
        [isMobileMenuOpen]="isMobileMenuOpen"
        (closeMenu)="onCloseMenu()"
        class="hidden sm:block lg:hidden">
      </app-medium-screen-menu>
    </div>
  `
})
export class TopNavComponent {
  @Input() navPages: NavPage[] = [];
  @Input() isMobileMenuOpen = false;
  @Output() toggleMenu = new EventEmitter<void>();
  @Output() closeMenu = new EventEmitter<void>();

  onToggleMenu() {
    this.toggleMenu.emit();
  }

  onCloseMenu() {
    this.closeMenu.emit();
  }
}