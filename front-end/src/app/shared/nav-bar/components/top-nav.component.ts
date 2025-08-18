import { Component, Input, Output, EventEmitter } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavPage } from '../nav-bar.component';
import { NavLogoComponent } from './nav-logo.component';
import { DesktopNavMenuComponent } from './desktop-nav-menu.component';
import { MobileMenuButtonComponent } from './mobile-menu-button.component';
import { MediumScreenMenuComponent } from './medium-screen-menu.component';
import { UserMenuComponent } from './user-menu.component';

@Component({
  selector: 'app-top-nav',
  imports: [
    RouterModule, 
    NavLogoComponent, 
    DesktopNavMenuComponent, 
    MobileMenuButtonComponent,
    MediumScreenMenuComponent,
    UserMenuComponent
  ],
  template: `
    <!-- Top nav bar -->
    <div class="z-10 w-full h-16 bg-gradient-to-r from-custom-nav-secondary via-custom-nav-via to-custom-nav-secondary 
                sm:bg-gradient-to-r sm:from-custom-nav-via sm:to-custom-nav-secondary shadow-lg">
      <div class="flex items-center sm:justify-between justify-center h-full px-6">
        
        <app-nav-logo></app-nav-logo>

        <div class="hidden sm:flex items-center space-x-4">
        <app-desktop-nav-menu class="hidden lg:flex items-center space-x-8" [navPages]="navPages"></app-desktop-nav-menu>
        <app-user-menu></app-user-menu>
          <app-mobile-menu-button
            [isMobileMenuOpen]="isMobileMenuOpen"
            (toggleMenu)="onToggleMenu()"
            class="lg:hidden">
          </app-mobile-menu-button>
        </div>
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