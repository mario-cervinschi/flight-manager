import { Component, ViewChild, ElementRef } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ServicesService } from '../../../../shared/services.service';
import { Airport } from '../../../../model/airport';
import { NgClass } from '@angular/common';
import { DropdownAirportsComponent } from './components/dropdown-airports.component';

@Component({
  selector: 'app-search-form',
  imports: [ReactiveFormsModule, NgClass, DropdownAirportsComponent],
  templateUrl: './search-form.component.html',
  styleUrl: './search-form.component.css'
})
export class SearchFormComponent {
  fg = new FormGroup({
    destinationForm: new FormControl('', Validators.required),
    departureForm: new FormControl('', Validators.required),
    departureDateForm: new FormControl('', Validators.required),
    returnDateForm: new FormControl(''),
    ticketsForm: new FormControl(1, [Validators.required, Validators.min(1)])
  });

  airports: Airport[] = [
    { code: 'OTP', name: 'Henri Coandă International Airport', city: 'București', country: 'România' },
    { code: 'CLJ', name: 'Cluj-Napoca International Airport', city: 'Cluj-Napoca', country: 'România' },
    { code: 'TSR', name: 'Timișoara Traian Vuia International Airport', city: 'Timișoara', country: 'România' },
    { code: 'IAS', name: 'Iași International Airport', city: 'Iași', country: 'România' },
    { code: 'CND', name: 'Mihail Kogălniceanu International Airport', city: 'Constanța', country: 'România' },
    { code: 'CDG', name: 'Charles de Gaulle Airport', city: 'Paris', country: 'Franța' },
    { code: 'LHR', name: 'Heathrow Airport', city: 'London', country: 'Marea Britanie' },
    { code: 'FCO', name: 'Leonardo da Vinci Airport', city: 'Roma', country: 'Italia' },
    { code: 'BCN', name: 'Barcelona-El Prat Airport', city: 'Barcelona', country: 'Spania' },
    { code: 'AMS', name: 'Amsterdam Airport Schiphol', city: 'Amsterdam', country: 'Olanda' },
    { code: 'FRA', name: 'Frankfurt Airport', city: 'Frankfurt', country: 'Germania' },
    { code: 'MUC', name: 'Munich Airport', city: 'München', country: 'Germania' },
    { code: 'VIE', name: 'Vienna International Airport', city: 'Viena', country: 'Austria' },
    { code: 'ZUR', name: 'Zurich Airport', city: 'Zurich', country: 'Elveția' },
    { code: 'IST', name: 'Istanbul Airport', city: 'Istanbul', country: 'Turcia' }
  ];

  filteredDestinations: Airport[] = [];
  filteredDepartures: Airport[] = [];
  
  isSelectingDeparture = false;
  isSelectingDestination = false;

  showDestinationDropdown = false;
  showDepartureDropdown = false;
  
  destinationSearchTerm = '';
  departureSearchTerm = '';
  
  selectedDestination: Airport | null = null;
  selectedDeparture: Airport | null = null;
  
  ticketOptions = [1, 2, 3, 4, 5, 6, 7, 8, 9];

  constructor(private service: ServicesService) {}

  onDestinationFocus() {
    this.filteredDestinations = this.airports;
    this.showDestinationDropdown = true;
  }

  onDestinationSearch(event: any) {
    const searchTerm = event.target.value.toLowerCase();
    this.destinationSearchTerm = searchTerm;
    
    if (searchTerm.length > 0) {
      this.filteredDestinations = this.airports.filter(airport =>
        airport.city.toLowerCase().includes(searchTerm) ||
        airport.name.toLowerCase().includes(searchTerm) ||
        airport.code.toLowerCase().includes(searchTerm) ||
        airport.country.toLowerCase().includes(searchTerm)
      );
    } else {
      this.filteredDestinations = this.airports;
    }
    this.showDestinationDropdown = true;
  }

  selectDestination(airport: Airport) {
    this.selectedDestination = airport;
    this.destinationSearchTerm = '';
    this.showDestinationDropdown = false;
    this.fg.get('destinationForm')?.setValue(airport.code);
  }

  // Functii pentru plecare
  onDepartureFocus() {
    this.filteredDepartures = this.airports;
    this.showDepartureDropdown = true;
  }

  onDepartureSearch(event: any) {
    const searchTerm = event.target.value.toLowerCase();
    this.departureSearchTerm = searchTerm;
    
    if (searchTerm.length > 0) {
      this.filteredDepartures = this.airports.filter(airport =>
        airport.city.toLowerCase().includes(searchTerm) ||
        airport.name.toLowerCase().includes(searchTerm) ||
        airport.code.toLowerCase().includes(searchTerm) ||
        airport.country.toLowerCase().includes(searchTerm)
      );
    } else {
      this.filteredDepartures = this.airports;
    }
    this.showDepartureDropdown = true;
  }

  onDepartureKeyDown(event: KeyboardEvent) {
    const isPrintableKey = event.key.length === 1; // 'a', '1', etc.
    const isDeletionKey = event.key === 'Backspace' || event.key === 'Delete';
  
    if (this.selectedDeparture && (isPrintableKey || isDeletionKey)) {
      this.clearDeparture();
  
      setTimeout(() => {
        const inputEl = event.target as HTMLInputElement;
  
        if (isPrintableKey) {
          this.departureSearchTerm = event.key;
          inputEl.value = event.key;
          this.onDepartureSearch({ target: { value: event.key } });
        } else if (isDeletionKey) {
          this.departureSearchTerm = '';
          inputEl.value = '';
          this.onDepartureSearch({ target: { value: '' } });
        }
      });
    }
  }

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
  
  

  // Închiderea dropdown-urilor când se face click în afara lor
  onBlur(type: 'destination' | 'departure') {
    setTimeout(() => {
      if (type === 'departure' && !this.isSelectingDeparture) {
        this.showDepartureDropdown = false;
        if (!this.selectedDeparture) {
          this.departureSearchTerm = '';
        }
      }
  
      if (type === 'destination' && !this.isSelectingDestination) {
        this.showDestinationDropdown = false;
        if (!this.selectedDestination) {
          this.destinationSearchTerm = '';
        }
      }
    }, 50);
  }
  

  // Resetarea selecției
  clearDestination() {
    this.selectedDestination = null;
    this.destinationSearchTerm = '';
    this.fg.get('destinationForm')?.setValue('');
  }

  clearDeparture() {
    this.selectedDeparture = null;
    this.departureSearchTerm = '';
    this.fg.get('departureForm')?.setValue('');
  }

  retrieveFormData(): FormGroup | null {
    this.fg.markAllAsTouched();

    if (this.fg.valid) {
      return this.fg;
    } else {
      return null;
    }
  }
}
