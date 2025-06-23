import { Component } from '@angular/core';

@Component({
  selector: 'app-mobile-promotion-main-widget',
  imports: [],
  template: `<div class="sm:hidden flex flex-col items-center">
    <div>
      <h1
        class="leading-none text-[44px] px-12 pt-4 font-extrabold text-white/95 text-center drop-shadow-xl"
      >
        CURRENT PROMOTION
      </h1>
      <h2
        class="text-[28px] px-12 mt-3 font-extrabold text-white/95 text-center drop-shadow-lg"
      >
        SMALL INFO -15%
      </h2>

      <div class="flex justify-center my-8">
        <button
          class="px-6 py-1.5 bg-emerald-600 hover:border-none hover:bg-emerald-700 border-[1px] border-custom-nav-via/15 text-white font-semibold text-md rounded-md transition duration-100 ease-in-out focus:outline-none focus:ring-2 focus:ring-emerald-700 focus:ring-opacity-50"
        >
          Learn More
        </button>
      </div>
    </div>
  </div> `,
  styles: ``,
})
export class MobilePromotionMainWidgetComponent {}
