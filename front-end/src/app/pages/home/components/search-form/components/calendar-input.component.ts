import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { GenericInputComponent } from './generic-input.component';
import { DropdownCalendarComponent } from './dropdown-calendar.component';
import { CalendarDay } from '../../../../../model/calendar_day';
import { Airport } from '../../../../../model/airport';

@Component({
  selector: 'app-calendar-input',
  imports: [GenericInputComponent, DropdownCalendarComponent],
  template: `<app-generic-input
      [inputLabel]="inputLabel"
      [inputType]="'Calendar'"
      (hideCurrentDropdown)="activeDropdown($event)"
    ></app-generic-input>

    <app-dropdown-calendar
      [showDropdown]="showCalendarDropdown"
      [availableDates]="dates"
      (dateSelected)="onDateSelected($event)"
      (monthChanged)="onMonthChanged($event)"
    ></app-dropdown-calendar>`,
  styles: ``,
})
export class CalendarInputComponent {
  @Input() inputLabel: string = 'NaN';
  @Input() dates: CalendarDay[] = [];
  @Input() selectedDepartureAirport: Airport | null = null;
  @Input() selectedArrivalAirport: Airport | null = null;

  @Output() onDateChanged = new EventEmitter<{ year: number; month: number }>();
  
  showCalendarDropdown: boolean = false;
  selectedDate?: Date;

  onDateSelected(date: Date) {
    this.selectedDate = date;
    console.log('Data plecare selectată:', date);
  }

  onMonthChanged(event: { year: number; month: number }) {
    // console.log('Luna schimbată pentru plecare:', event);
    // this.loadDatesForMonth(event.year, event.month);
    this.onDateChanged.emit(event);
  }

  activeDropdown(value: boolean) {
    this.showCalendarDropdown = !value;
  }
}
