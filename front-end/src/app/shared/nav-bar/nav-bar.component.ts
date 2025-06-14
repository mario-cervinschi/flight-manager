import { Component } from '@angular/core';
import { TopNavComponent } from './components/top-nav.component';
import { BottomNavComponent } from './components/bottom-nav.component';
import { MobileMenuOverlayComponent } from './components/mobile-menu-overlay.component';

export interface NavPage {
  page: string;
  url: string;
}

@Component({
  selector: 'app-nav-bar',
  imports: [TopNavComponent, BottomNavComponent, MobileMenuOverlayComponent],
  template: `
    <app-top-nav 
      [navPages]="navPages"
      [isMobileMenuOpen]="isMobileMenuOpen"
      (toggleMenu)="toggleMobileMenu()"
      (closeMenu)="closeMobileMenu()">
    </app-top-nav>

    <app-bottom-nav 
      [isMobileMenuOpen]="isMobileMenuOpen"
      (toggleMenu)="toggleMobileMenu()">
    </app-bottom-nav>

    <app-mobile-menu-overlay 
      [navPages]="navPages"
      [isMobileMenuOpen]="isMobileMenuOpen"
      (closeMenu)="closeMobileMenu()">
    </app-mobile-menu-overlay>
  `,
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent {
  navPages: NavPage[] = [
    { page: 'Home', url: '/' },
    { page: 'Flights', url: '/flights' },
    { page: 'Test1', url: '/flights' },
    { page: 'Test2', url: '/flights' },
    { page: 'Test3', url: '/flights' },
    { page: 'Test4', url: '/flights' },
  ];

  isMobileMenuOpen = false;

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
    this.toggleBodyScroll();
  }

  closeMobileMenu() {
    this.isMobileMenuOpen = false;
    this.enableBodyScroll();
  }

  private toggleBodyScroll() {
    if (this.isMobileMenuOpen) {
      document.body.classList.add('overflow-hidden');
    } else {
      document.body.classList.remove('overflow-hidden');
    }
  }
  
  private enableBodyScroll() {
    document.body.classList.remove('overflow-hidden');
  }
}