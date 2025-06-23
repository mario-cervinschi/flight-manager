import { Component } from '@angular/core';
import { SearchFormComponent } from '../search-form/search-form.component';
import { DesktopPromotionMainWidgetComponent } from './desktop-promotion.component';
import { MobilePromotionMainWidgetComponent } from './mobile-promotion.component';

@Component({
  selector: 'app-main-widget',
  imports: [
    SearchFormComponent,
    DesktopPromotionMainWidgetComponent,
    MobilePromotionMainWidgetComponent,
  ],
  template: `
    <div class="relative w-full min-h-screen overflow-hidden">
      <div class="absolute inset-0 z-0">
        <img
          src="main_widget_background.png"
          alt=""
          class="w-full h-full object-cover"
        />
      </div>

      <div class="relative z-10 lg:px-28 sm:pt-12 pt-0 px-2">
        <app-mobile-promotion-main-widget></app-mobile-promotion-main-widget>
        <app-search-form></app-search-form>
        <app-desktop-promotion-main-widget></app-desktop-promotion-main-widget>
      </div>
    </div>
  `,
  styles: ``,
})
export class MainWidgetComponent {}
