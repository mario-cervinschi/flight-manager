import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  imports: [RouterModule],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent {
  navPages : { page: string; url: string }[] = [
    {page: 'Home', url: '/'},
    {page: 'Flights', url: '/flights'},
    {page: 'Test1', url: '/flights'},
    {page: 'Test2', url: '/flights'},
    {page: 'Test3', url: '/flights'},
    {page: 'Test4', url: '/flights'},
  ];

  isMobileMenuOpen = false;

  toggleMobileMenu(){
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
    this.toggleBodyScroll();
  }

  closeMobileMenu(){
    this.isMobileMenuOpen = false;
    this.enableBodyScroll();
  }

  toggleBodyScroll() {
    if (this.isMobileMenuOpen) {
      document.body.classList.add('overflow-hidden');
    } else {
      document.body.classList.remove('overflow-hidden');
    }
  }
  
  enableBodyScroll() {
    document.body.classList.remove('overflow-hidden');
  }

}
