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
import { TicketInputComponent } from './components/ticket-input.component';
import { TicketCounter } from '../../../../model/ticket_counter';

@Component({
  selector: 'app-search-form',
  imports: [
    ReactiveFormsModule,
    AirportInputComponent,
    CalendarInputComponent,
    TicketInputComponent,
  ],
  templateUrl: './search-form.component.html',
  styleUrl: './search-form.component.css',
})
export class SearchFormComponent {
  @ViewChild('departureInput') departureInput!: AirportInputComponent;
  @ViewChild('destinationInput') destinationInput!: AirportInputComponent;
  @ViewChild('departureDateInput') departureDateInput!: CalendarInputComponent;
  @ViewChild('ticketInput') ticketInput!: TicketInputComponent;

  fg = new FormGroup({
    destinationForm: new FormControl(''),
    departureForm: new FormControl('', Validators.required),
    departureDateForm: new FormControl('', Validators.required),
    returnDateForm: new FormControl(''),
    ticketsForm: new FormControl(
      { adults: 0, children: 0, infants: 0 },
      Validators.required
    ),
  });

  airports: Airport[] = [];
  destinationAirports: Airport[] = [];
  
  datesGoTo: CalendarDay[] = [];
  datesReturn: CalendarDay[] = [];

  selectedDestination: Airport | null = null;
  selectedDeparture: Airport | null = null;

  selectedDepartureDate: Date | null = null;
  selectedReturnDate: Date | null = null;

  selectedTicketNumbers: TicketCounter | null = null;

  isLoadingDestinations = false;

  constructor(private service: ServicesService) {
    this.getAllAirports();
  }

  private async getAllAirports() {
    try {
      this.airports = await this.service.getAllAirpots();
    } catch (error) {
      console.error('Error on loading airports: ', error);
    }
  }

  async getNewDatesGoTo(event: { month: number; year: number }) {
    if (this.selectedDeparture && this.selectedDestination) {
      this.datesGoTo = await this.service.getTravelDays(
        this.selectedDeparture,
        this.selectedDestination,
        event.year,
        event.month
      );
    }
  }

  async getNewDatesReturn(event: { month: number; year: number }) {
    if (this.selectedDeparture && this.selectedDestination) {
      this.datesReturn = await this.service.getTravelDays(
        this.selectedDestination,
        this.selectedDeparture,
        event.year,
        event.month
      );
    }
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
      if (this.selectedDestination) {
        const now = new Date();
        await this.getNewDatesGoTo({
          year: now.getFullYear(),
          month: now.getMonth() + 1,
        });
        await this.getNewDatesReturn({
          year: now.getFullYear(),
          month: now.getMonth() + 1,
        });
      }
      setTimeout(() => this.destinationInput?.focus(), 0);
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

  selectTicketNumber(event: TicketCounter) {
    this.fg.get('ticketsForm')?.setValue(event);
    this.selectedTicketNumbers = event;
  }

  async selectDestination(airport: Airport | null) {
    this.selectedDestination = airport;
    this.fg.patchValue({ destinationForm: airport ? airport.code : '' });

    if (airport && this.selectedDeparture) {
      const now = new Date();
      await this.getNewDatesGoTo({
        year: now.getFullYear(),
        month: now.getMonth() + 1,
      });
      await this.getNewDatesReturn({
        year: now.getFullYear(),
        month: now.getMonth() + 1,
      });
    }

    setTimeout(() => this.departureDateInput?.focus(), 0);
  }

  @ViewChild('returnDateInput') returnDateInput!: CalendarInputComponent;

  swapAirports() {
    const temp = this.selectedDeparture;
    this.selectedDeparture = this.selectedDestination;
    this.selectedDestination = temp;

    this.fg.patchValue({
      departureForm: this.selectedDeparture ? this.selectedDeparture.code : '',
      destinationForm: this.selectedDestination
        ? this.selectedDestination.code
        : '',
      departureDateForm: '',
      returnDateForm: '',
    });

    this.departureInput.setCurrentAirport(this.selectedDeparture);
    this.destinationInput.setCurrentAirport(this.selectedDestination);

    if (this.selectedDeparture) {
      this.loadDestinationAirports(this.selectedDeparture.code);
    } else {
      this.destinationAirports = [];
    }

    this.departureDateInput?.clearSelectedDate?.();
    this.returnDateInput?.clearSelectedDate?.();
  }

  selectDepartureDate(departureDate: Date | null) {
    this.selectedDepartureDate = departureDate;
    this.fg.patchValue({
      departureDateForm: departureDate ? departureDate.toISOString() : '',
    });

    if (this.selectedReturnDate) {
      this.selectedReturnDate = null;
      this.fg.patchValue({
        returnDateForm: '',
      });
      this.returnDateInput.clearSelectedDate();
    }
    setTimeout(() => this.returnDateInput?.focus(), 0);
  }

  selectReturnDate(returnDate: Date | null) {
    this.selectedReturnDate = returnDate;
    this.fg.patchValue({
      returnDateForm: returnDate ? returnDate.toISOString() : '',
    });
    setTimeout(() => this.ticketInput?.focus(), 0);
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
