import { Component, Input } from '@angular/core';
import { Airport } from '../../../../../../model/airport';

@Component({
  selector: 'app-dropdown-airports',
  imports: [],
  template: `
    @if (showDepartureDropdown) {
        <div
          class="absolute z-50 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-y-auto"
        >
          @for (airport of airports; track airport.code) {
          <div
            class="px-3 py-2 hover:bg-emerald-50 cursor-pointer border-b border-gray-100 last:border-b-0"
            (mousedown)="selectDepartureWithDelay(airport)"
          >
            <div class="font-medium text-gray-900">
              {{ airport.city }} ({{ airport.code }})
            </div>
            <div class="text-sm text-gray-600">{{ airport.name }}</div>
            <div class="text-xs text-gray-500">{{ airport.country }}</div>
          </div>
          } @empty {
          <div class="px-3 py-2 text-gray-500 text-center">
            Nu s-au găsit rezultate
          </div>
          }
        </div>
        }
  `,
  styles: ``
})
export class DropdownAirportsComponent {
  @Input() showDepartureDropdown = false;
  @Input() airports: Airport[] = [];

  selectDeparture(airport: Airport) {
    this.selectedDeparture = airport;
    this.departureSearchTerm = '';
    this.showDepartureDropdown = false;
    this.fg.get('departureForm')?.setValue(airport.code);
  }

  selectDepartureWithDelay(airport: Airport) {
    this.isSelectingDeparture = true;
  
    let mouseUpDetected = false;
    let timeoutId: any;
  
    const mouseUpListener = () => {
      mouseUpDetected = true;
      document.removeEventListener('mouseup', mouseUpListener);
      clearTimeout(timeoutId);
  
      this.selectDeparture(airport); // selectează doar dacă mouseup-ul e în limita timpului
      setTimeout(() => {
        this.isSelectingDeparture = false;
      }, 10);
    };
  
    document.addEventListener('mouseup', mouseUpListener);
  
    // Dacă mouseul nu e ridicat în 3.5 secunde, considerăm că selecția e invalidă
    timeoutId = setTimeout(() => {
      document.removeEventListener('mouseup', mouseUpListener);
      if (!mouseUpDetected) {
        console.log('Mouse held too long, selection cancelled');
        this.isSelectingDeparture = false;
      }
    }, 1000); // 3.5 secunde limită
  }
}
