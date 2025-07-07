import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { DropdownAirportsComponent } from './dropdown-airports.component';
import { Airport } from '../../../../../model/airport';
import { GenericInputComponent } from './generic-input.component';
import { AirportMapComponent } from './airport-map.component';

@Component({
  selector: 'app-input-airports',
  imports: [
    DropdownAirportsComponent,
    GenericInputComponent,
    AirportMapComponent,
  ],
  template: `
    @if(showMap){
    <app-airport-map
      class="z-[9999]"
      [airports]="filteredAirports"
      (closed)="shouldShowMap()"
      (selectedAirport)="select($event)"
    ></app-airport-map>
    }

    <!-- generic input -->
    <app-generic-input
      [inputLabel]="inputLabel"
      [inputType]="'Airport'"
      [currentObject]="currentAirport"
      (inputSearchTermChange)="onSearch($event)"
      (hideCurrentDropdown)="activeDropdown($event)"
    ></app-generic-input>

    <!-- Dropdown -->
    <app-dropdown-airports
      [airports]="filteredAirports"
      [showDropdown]="showAirportDropdown"
      (shouldShowMap)="shouldShowMap()"
      (selectedAirport)="select($event)"
    ></app-dropdown-airports>
  `,
  styles: ``,
})
export class AirportInputComponent {
  @ViewChild(GenericInputComponent)
  genericInputComponent!: GenericInputComponent;

  focus() {
    this.genericInputComponent?.focusInput();
  }

  @Input() inputLabel: string = '';
  @Input() airports: Airport[] = [];
  @Input() showMap: boolean = false;

  @Output() selectedAirport = new EventEmitter<Airport | null>();
  @Output() inputLabelChange = new EventEmitter<string>();

  searchWords = '';

  currentAirport: Airport | null = null;
  filteredAirports: Airport[] = [];
  showAirportDropdown: boolean = false;

  shouldShowMap() {
    this.showMap = !this.showMap;
    this.showAirportDropdown = false;

    if (this.showMap) {
      document.body.classList.add('overflow-hidden');
    } else {
      document.body.classList.remove('overflow-hidden');
    }
  }

  activeDropdown(value: boolean) {
    this.showAirportDropdown = !value;
    if (this.searchWords.length === 0) {
      this.filteredAirports = this.airports;
    }
  }

  select(airport: Airport) {
    this.currentAirport = airport;
    this.showAirportDropdown = false;
    this.selectedAirport.emit(airport);
  }

  setCurrentAirport(airport: Airport | null) {
    this.currentAirport = airport;
  }

  onSearch(event: any) {
    const searchTermWord = event.target.value.toLowerCase();

    this.searchWords = searchTermWord;

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
}
