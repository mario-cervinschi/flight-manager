import { Component } from '@angular/core';

@Component({
  selector: 'app-desktop-promotion-main-widget',
  imports: [],
  template: `<div class="sm:flex hidden">
    <div class="">
      <h1
        class="md:text-[50px] text-[44px] lg:px-4 px-12 pt-4 font-extrabold text-white/95 text-left drop-shadow-xl"
      >
        CURRENT PROMOTION
      </h1>
      <h2
        class="text-[28px] lg:px-4 px-12 -mt-3 font-extrabold text-white/95 text-left drop-shadow-lg"
      >
        SMALL INFO -15%
      </h2>
      <p
      class="leading-tight max-w-[750px] md:pt-20 pt-12 text-xl lg:px-4 px-12 font-bold text-white/95 text-justify drop-shadow-lg"
      >
        Pellentesque habitant morbi tristique senectus et netus et malesuada
        fames ac turpis egestas.
      </p>

      <button class="px-6 lg:ml-4 ml-12 py-1.5 mt-8 bg-emerald-600 hover:bg-emerald-700 text-white font-semibold text-md rounded-md transition duration-100 ease-in-out focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-opacity-50">Learn More</button>
    </div>
  </div>`,
  styles: ``,
})
export class DesktopPromotionMainWidgetComponent {}
