import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Airport } from '../../../../../model/airport';
import {
  groupAirportsByCountry,
  GroupedAirports,
} from '../../../../../model/grouped_airports';

@Component({
  selector: 'app-dropdown-airports',
  imports: [],
  template: `
    @if (showDropdown) {
    <div
      class="custom-scrollbar absolute z-50 w-[130%] max-w-[300px] min-w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-y-auto select-none"
      (mousedown)="onMouseDown($event)"
    >
      @for (group of groupedAirports; track group.country) {
      <!-- Country name -->
      <div class="px-3 py-2 bg-gray-100 border-b border-gray-200 sticky">
        <div class="font-bold text-gray-700 text-sm uppercase tracking-wide">
          {{ group.country }}
        </div>
      </div>

      <!-- Airports in this country -->
      @for (airport of group.airports; track airport.code) {
      <div
        class="px-4 py-2 hover:bg-emerald-50 cursor-pointer border-b border-gray-50 last:border-b-0"
        (mousedown)="select(airport)"
      >
        <div class="font-medium text-gray-900">
          {{ airport.city }} ({{ airport.code }})
        </div>
        <div class="text-sm text-gray-600">{{ airport.name }}</div>
      </div>
      } } @empty {
      <div class="px-3 py-2 text-gray-500 text-center">No results found.</div>
      }
    </div>
    }
  `,
  styles: `.custom-scrollbar::-webkit-scrollbar {
    width: 7px;
  }
  
  .custom-scrollbar::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.4);
    border-radius: 7px;
  }
  
  .custom-scrollbar::-webkit-scrollbar-track {
    background: transparent;
  }`,
})
export class DropdownAirportsComponent {
  @Input() showDropdown = false;
  @Input() airports: Airport[] = [];

  @Output() selectedAirport = new EventEmitter<Airport>();

  get groupedAirports(): GroupedAirports[] {
    return groupAirportsByCountry(this.airports);
  }

  onMouseDown(event: MouseEvent) {
    event.preventDefault();
  }

  select(airport: Airport) {
    this.showDropdown = false;
    this.selectedAirport.emit(airport);
  }
}
