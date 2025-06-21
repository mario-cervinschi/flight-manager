import { Component, ViewChild } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ServicesService } from '../../../../shared/services.service';
import { Airport } from '../../../../model/airport';
import { AirportInputComponent } from './components/airport-input.component';
import { CalendarInputComponent } from './components/calendar-input.component';
import { CalendarDay } from '../../../../model/calendar_day';

@Component({
  selector: 'app-search-form',
  imports: [ReactiveFormsModule, AirportInputComponent, CalendarInputComponent],
  templateUrl: './search-form.component.html',
  styleUrl: './search-form.component.css',
})
export class SearchFormComponent {
  fg = new FormGroup({
    destinationForm: new FormControl(''),
    departureForm: new FormControl('', Validators.required),
    departureDateForm: new FormControl('', Validators.required),
    returnDateForm: new FormControl(''),
    ticketsForm: new FormControl(1, [Validators.required, Validators.min(1)]),
  });

  airports: Airport[] = [
    {
      code: 'OTP',
      name: 'Henri Coandă International Airport',
      city: 'București',
      country: 'Romania',
    },
    {
      code: 'CLJ',
      name: 'Cluj-Napoca International Airport',
      city: 'Cluj-Napoca',
      country: 'Romania',
    },
    {
      code: 'TSR',
      name: 'Timișoara Traian Vuia International Airport',
      city: 'Timișoara',
      country: 'Romania',
    },
    {
      code: 'IAS',
      name: 'Iași International Airport',
      city: 'Iași',
      country: 'Romania',
    },
    {
      code: 'CND',
      name: 'Mihail Kogălniceanu International Airport',
      city: 'Constanța',
      country: 'Romania',
    },
    {
      code: 'CDG',
      name: 'Charles de Gaulle Airport',
      city: 'Paris',
      country: 'Franta',
    },
    {
      code: 'LHR',
      name: 'Heathrow Airport',
      city: 'London',
      country: 'Marea Britanie',
    },
    {
      code: 'FCO',
      name: 'Leonardo da Vinci Airport',
      city: 'Roma',
      country: 'Italia',
    },
    {
      code: 'BCN',
      name: 'Barcelona-El Prat Airport',
      city: 'Barcelona',
      country: 'Spania',
    },
    {
      code: 'AMS',
      name: 'Amsterdam Airport Schiphol',
      city: 'Amsterdam',
      country: 'Olanda',
    },
    {
      code: 'FRA',
      name: 'Frankfurt Airport',
      city: 'Frankfurt',
      country: 'Germania',
    },
    {
      code: 'MUC',
      name: 'Munich Airport',
      city: 'München',
      country: 'Germania',
    },
    {
      code: 'VIE',
      name: 'Vienna International Airport',
      city: 'Viena',
      country: 'Austria',
    },
    { code: 'ZUR', name: 'Zurich Airport', city: 'Zurich', country: 'Elvetia' },
    {
      code: 'IST',
      name: 'Istanbul Airport',
      city: 'Istanbul',
      country: 'Turcia',
    },
  ];

  destinationAirports: Airport[] = [];
  dates: CalendarDay[] = [];

  selectedDestination: Airport | null = null;
  selectedDeparture: Airport | null = null;

  selectedDepartureDate: Date | null = null;
  selectedReturnDate: Date | null = null;

  isLoadingDestinations = false;

  ticketOptions = [1, 2, 3, 4, 5, 6, 7, 8, 9];

  constructor(private service: ServicesService) {
    this.dates = this.service.generateMockDates(
      new Date().getFullYear(),
      new Date().getMonth() + 1
    );
  }

  async selectDeparture(airport: Airport | null) {
    this.selectedDeparture = airport;
    this.fg.patchValue({ departureForm: airport ? airport.code : '' });

    if (this.selectedDestination) {
      this.selectedDestination = null;
      this.fg.patchValue({
        destinationForm: '',
      });
    }

    if (airport) {
      await this.loadDestinationAirports(airport.code);
    } else {
      this.destinationAirports = [];
    }
  }

  private async loadDestinationAirports(departureCode: string) {
    try {
      this.isLoadingDestinations = true;

      this.destinationAirports = await this.service.getDestinationAirports(
        departureCode
      );
    } catch (error) {
      console.error('Error loading destination airports:', error);
      this.destinationAirports = [];
    } finally {
      this.isLoadingDestinations = false;
    }
  }

  getNewDates(event: { year: number; month: number }) {
    this.dates = this.service.generateMockDates(event.year, event.month);
  }

  selectDestination(airport: Airport | null) {
    this.selectedDestination = airport;
    this.fg.patchValue({ destinationForm: airport ? airport.code : '' });
  }

  @ViewChild('returnDateInput') returnDateInput!: CalendarInputComponent;

  selectDepartureDate(departureDate: Date | null) {
    this.selectedDepartureDate = departureDate;
    this.fg.patchValue({ departureDateForm: departureDate ? departureDate.toISOString() : '' });

    if (this.selectedReturnDate) {
      this.selectedReturnDate = null;
      this.fg.patchValue({
        returnDateForm: '',
      });
      this.returnDateInput.clearSelectedDate();
    }
  }

  selectReturnDate(returnDate: Date | null) {
    this.selectedReturnDate = returnDate;
    this.fg.patchValue({ returnDateForm: returnDate ? returnDate.toISOString() : '' });
  }

  retrieveFormData(): FormGroup | null {
    this.fg.markAllAsTouched();

    if (this.fg.valid) {
      console.log(this.fg);
      return this.fg;
    } else {
      return null;
    }
  }
}
