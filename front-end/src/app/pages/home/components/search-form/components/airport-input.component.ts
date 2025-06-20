import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DropdownAirportsComponent } from './dropdown-airports.component';
import { Airport } from '../../../../../model/airport';

@Component({
  selector: 'app-input-airports',
  imports: [DropdownAirportsComponent],
  template: `
    <label
      class="absolute pl-3 pt-1 text-sm font-medium text-custom-nav-via/80 mb-2 z-10 transition-opacity duration-200"
      [class.opacity-100]="searchTerm || currentAirport"
      [class.opacity-0]="!searchTerm && !currentAirport"
    >
      {{ currentAirport || searchTerm ? this.inputLabel : '' }}
    </label>
    <div class="relative">
      <input
        type="text"
        class="w-full px-4 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 bg-gray-50 transition-all placeholder-custom-nav-primary/50 text-custom-nav-via/90 font-semibold text-lg"
        [placeholder]="
          currentAirport
            ? currentAirport.city + ' (' + currentAirport.code + ')'
            : inputLabel
        "
        [value]="
          currentAirport
            ? currentAirport.city + ' (' + currentAirport.code + ')'
            : searchTerm
        "
        [class.pt-5]="searchTerm || currentAirport"
        [class.pb-1]="searchTerm || currentAirport"
        [class.py-3.5]="!searchTerm && !currentAirport"
        [class.text-gray-400]="!currentAirport"
        (input)="onSearch($event)"
        (keydown)="onKeyDown($event)"
        (focus)="onDepartureFocus()"
        (blur)="onBlur()"
      />
    </div>

    <!-- Dropdown -->
    <app-dropdown-airports
      [showDropdown]="showAirportDropdown"
      [airports]="filteredAirports"
      (selectedAirport)="select($event)"
    ></app-dropdown-airports>
  `,
  styles: ``,
})
export class AirportInputComponent {
  @Input() inputLabel: string = '';
  @Input() airports: Airport[] = [];
  @Input() disabled: boolean = false;

  @Output() selectedAirport = new EventEmitter<Airport | null>();

  searchTerm = '';
  currentAirport: Airport | null = null;
  filteredAirports: Airport[] = [];
  showAirportDropdown: boolean = false;

  select(airport: Airport) {
    this.currentAirport = airport;
    this.searchTerm = '';
    this.showAirportDropdown = false;
    this.selectedAirport.emit(airport);
  }

  onSearch(event: any) {
    const searchTermWord = event.target.value.toLowerCase();
    this.searchTerm = searchTermWord;

    if (searchTermWord.length > 0) {
      this.filteredAirports = this.airports.filter(
        (airport) =>
          airport.city.toLowerCase().includes(searchTermWord) ||
          airport.name.toLowerCase().includes(searchTermWord) ||
          airport.code.toLowerCase().includes(searchTermWord) ||
          airport.country.toLowerCase().includes(searchTermWord)
      );
    } else {
      this.filteredAirports = this.airports;
    }
    this.showAirportDropdown = true;
  }

  onKeyDown(event: KeyboardEvent) {
    const isPrintableKey = event.key.length === 1;
    const isDeletionKey = event.key === 'Backspace' || event.key === 'Delete';

    if (this.currentAirport && (isPrintableKey || isDeletionKey)) {
      this.clearInput();

      setTimeout(() => {
        const inputEl = event.target as HTMLInputElement;

        if (isPrintableKey) {
          this.searchTerm = event.key;
          inputEl.value = event.key;
          this.onSearch({ target: { value: event.key } });
        } else if (isDeletionKey) {
          this.searchTerm = '';
          inputEl.value = '';
          this.onSearch({ target: { value: '' } });
        }
      });
    }
  }

  clearInput() {
    this.currentAirport = null;
    this.selectedAirport.emit(null);
    this.searchTerm = '';
  }

  onDepartureFocus() {
    if(this.disabled) return;

    this.filteredAirports = this.airports;
    this.showAirportDropdown = true;
  }

  onBlur() {
    setTimeout(() => {
      this.showAirportDropdown = false;
      if (!this.currentAirport && !this.searchTerm) {
        this.searchTerm = '';
      }
    }, 50);
  }
}
