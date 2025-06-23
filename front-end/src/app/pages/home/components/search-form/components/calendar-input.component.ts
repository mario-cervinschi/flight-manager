import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
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
      [currentObject]="selectedDate"
      (hideCurrentDropdown)="activeDropdown($event)"
    ></app-generic-input>

    <app-dropdown-calendar
      [showDropdown]="showCalendarDropdown"
      [availableDates]="dates"
      (dateSelected)="onDateSelected($event)"
      (monthChanged)="onNewMonth($event)"
    ></app-dropdown-calendar>`,
  styles: ``,
})
export class CalendarInputComponent {
  @ViewChild(GenericInputComponent) genericInputComponent!: GenericInputComponent;

  focus(){
    this.genericInputComponent?.focusInput();
  }
  
  @Input() inputLabel: string = 'NaN';
  @Input() dates: CalendarDay[] = [];
  @Input() selectedDepartureAirport: Airport | null = null;
  @Input() selectedArrivalAirport: Airport | null = null;

  @Output() onMonthChanged = new EventEmitter<{ year: number; month: number }>();
  @Output() outputSelectedDate = new EventEmitter<Date | null>();

  showCalendarDropdown: boolean = false;
  selectedDate: Date | null = null;

  onDateSelected(date: Date) {
    this.selectedDate = date;
    this.outputSelectedDate.emit(date);
  }

  clearSelectedDate(){
    this.selectedDate = null;
  }

  onNewMonth(event: { year: number; month: number }) {
    this.onMonthChanged.emit(event);
  }

  activeDropdown(value: boolean) {
    this.showCalendarDropdown = !value;
  }
}
