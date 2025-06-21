import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { CalendarDay } from '../../../../../model/calendar_day';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dropdown-calendar',
  imports: [CommonModule],
  template: `
    @if (showDropdown) {
    <div class="calendar-header">
      <button
        type="button"
        class="nav-button"
        (mousedown)="onMouseDown($event)"
        (click)="previousMonth()"
      >
        ‹
      </button>
      <span class="month-year">
        {{ monthNames[currentMonth] }} {{ currentYear }}
      </span>
      <button
        type="button"
        class="nav-button"
        (mousedown)="onMouseDown($event)"
        (click)="nextMonth()"
      >
        ›
      </button>
    </div>

    <!-- Zilele săptămânii -->
    <div class="calendar-weekdays">
      <div *ngFor="let day of weekDays" class="weekday">{{ day }}</div>
    </div>

    <!-- Zilele lunii -->
    <div class="calendar-days">
      <div
        *ngFor="let day of calendarDays"
        class="calendar-day"
        [class.enabled]="day.enabled"
        [class.disabled]="!day.enabled"
        [class.other-month]="!isCurrentMonth(day.date)"
        [class.today]="isToday(day.date)"
        (click)="selectDate(day)"
      >
        <span class="day-number">{{ day.date.getDate() }}</span>
        <span *ngIf="day.price && day.enabled" class="day-price">
          {{ day.price }} RON
        </span>
      </div>
    </div>
    }
  `,
  styles: `/* generic-input.component.scss */
  .input-container {
    position: relative;
    margin-bottom: 20px;
  }
  
  .input-label {
    display: block;
    margin-bottom: 5px;
    font-weight: 500;
    color: #333;
  }
  
  .input-wrapper {
    position: relative;
    cursor: pointer;
  }
  
  .custom-input {
    width: 100%;
    padding: 12px 40px 12px 16px;
    border: 2px solid #e1e5e9;
    border-radius: 8px;
    font-size: 16px;
    background: white;
    transition: border-color 0.2s ease;
    cursor: pointer;
  
    &:focus {
      outline: none;
      border-color: #2563eb;
    }
  
    &[readonly] {
      cursor: pointer;
    }
  }
  
  .dropdown-icon {
    position: absolute;
    right: 12px;
    top: 50%;
    transform: translateY(-50%);
    transition: transform 0.2s ease;
    color: #666;
    
    &.rotated {
      transform: translateY(-50%) rotate(180deg);
    }
  }
  
  .dropdown {
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    background: white;
    border: 2px solid #e1e5e9;
    border-top: none;
    border-radius: 0 0 8px 8px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    z-index: 1000;
    max-height: 400px;
    overflow-y: auto;
  }
  
  /* Stiluri pentru dropdown aeroporturi */
  .airport-dropdown {
    .dropdown-item {
      padding: 12px 16px;
      cursor: pointer;
      transition: background-color 0.2s ease;
      border-bottom: 1px solid #f1f5f9;
  
      &:hover {
        background-color: #f8fafc;
      }
  
      &:last-child {
        border-bottom: none;
      }
    }
  
    .airport-info {
      display: flex;
      align-items: center;
      gap: 12px;
    }
  
    .airport-code {
      font-weight: 600;
      color: #2563eb;
      min-width: 50px;
    }
  
    .airport-name {
      flex: 1;
      color: #333;
    }
  
    .airport-city {
      color: #666;
      font-size: 14px;
    }
  
    .no-results {
      padding: 16px;
      text-align: center;
      color: #666;
      font-style: italic;
    }
  }
  
  /* Stiluri pentru calendar */
  .calendar-dropdown {
    padding: 16px;
  }
  
  .calendar-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;
  }
  
  .nav-button {
    background: none;
    border: none;
    font-size: 20px;
    cursor: pointer;
    padding: 8px;
    border-radius: 4px;
    transition: background-color 0.2s ease;
  
    &:hover {
      background-color: #f1f5f9;
    }
  }
  
  .month-year {
    font-weight: 600;
    font-size: 16px;
    color: #333;
  }
  
  .calendar-weekdays {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 4px;
    margin-bottom: 8px;
  }
  
  .weekday {
    text-align: center;
    font-weight: 600;
    color: #666;
    padding: 8px 4px;
    font-size: 12px;
  }
  
  .calendar-days {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 2px;
  }
  
  .calendar-day {
    min-height: 60px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 4px;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.2s ease;
    position: relative;
  
    &.enabled {
      background-color: #f8fafc;
      border: 1px solid #e2e8f0;
  
      &:hover {
        background-color: #2563eb;
        color: white;
        transform: scale(1.05);
      }
    }
  
    &.disabled {
      color: #cbd5e1;
      cursor: not-allowed;
      background-color: #f9fafb;
    }
  
    &.other-month {
      opacity: 0.3;
    }
  
    &.today {
      border: 2px solid #2563eb;
      font-weight: 600;
    }
  }
  
  .day-number {
    font-size: 14px;
    font-weight: 500;
    margin-bottom: 2px;
  }
  
  .day-price {
    font-size: 10px;
    font-weight: 600;
    color: #16a34a;
    background: rgba(34, 197, 94, 0.1);
    padding: 1px 4px;
    border-radius: 4px;
  }
  
  .dropdown-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 999;
  }
  
  /* Responsive */
  @media (max-width: 640px) {
    .calendar-day {
      min-height: 45px;
      font-size: 12px;
    }
    
    .day-price {
      font-size: 8px;
    }
  }`,
})
export class DropdownCalendarComponent implements OnInit {
  @Input() showDropdown = false;
  @Input() availableDates: CalendarDay[] = [];

  @Output() dateSelected = new EventEmitter<Date>();
  @Output() monthChanged = new EventEmitter<{ year: number; month: number }>();

  currentDate = new Date();
  currentMonth = new Date().getMonth();
  currentYear = new Date().getFullYear();
  weekDays = ['L', 'M', 'M', 'J', 'V', 'S', 'D'];
  monthNames = [
    'Ianuarie',
    'Februarie',
    'Martie',
    'Aprilie',
    'Mai',
    'Iunie',
    'Iulie',
    'August',
    'Septembrie',
    'Octombrie',
    'Noiembrie',
    'Decembrie',
  ];

  calendarDays: CalendarDay[] = [];

  inputValue = '';

  ngOnInit() {
    this.generateCalendar();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['availableDates']) {
      this.generateCalendar();
    }
  }

  generateCalendar() {
    const firstDay = new Date(this.currentYear, this.currentMonth, 1);
    const lastDay = new Date(this.currentYear, this.currentMonth + 1, 0);
    const startDate = new Date(firstDay);

    // Ajustăm pentru prima zi a săptămânii (Luni = 0)
    const firstDayOfWeek = (firstDay.getDay() + 6) % 7;
    startDate.setDate(startDate.getDate() - firstDayOfWeek);

    this.calendarDays = [];

    for (let i = 0; i < 42; i++) {
      // 6 săptămâni x 7 zile
      const currentDate = new Date(startDate);
      currentDate.setDate(startDate.getDate() + i);

      const availableDate = this.availableDates.find(
        (d) => d.date.toDateString() === currentDate.toDateString()
      );

      this.calendarDays.push({
        date: new Date(currentDate),
        enabled: availableDate ? availableDate.enabled : false,
        price: availableDate?.price,
      });
    }
  }

  onMouseDown(event: MouseEvent) {
    event.preventDefault(); // Prevents input from losing focus
  }

  selectDate(day: CalendarDay) {
    if (day.enabled) {
      this.inputValue = day.date.toLocaleDateString('ro-RO');
      this.dateSelected.emit(day.date);
      this.showDropdown = false;
    }
  }

  private requestNextMonthData() {
    this.monthChanged.emit({
      year: this.currentYear,
      month: this.currentMonth + 1, // API-ul poate să aștepte 1-12 în loc de 0-11
    });
  }

  private requestPrevMonthData() {
    this.monthChanged.emit({
      year: this.currentYear,
      month: this.currentMonth - 1, // API-ul poate să aștepte 1-12 în loc de 0-11
    });
  }


  previousMonth() {
    if (this.currentMonth === 0) {
      this.currentMonth = 11;
      this.currentYear--;
    } else {
      this.currentMonth--;
    }
    this.requestPrevMonthData();
  }

  nextMonth() {
    if (this.currentMonth === 11) {
      this.currentMonth = 0;
      this.currentYear++;
    } else {
      this.currentMonth++;
    }
    this.requestNextMonthData();
  }

  isCurrentMonth(date: Date): boolean {
    return date.getMonth() === this.currentMonth;
  }

  isToday(date: Date): boolean {
    const today = new Date();
    return date.toDateString() === today.toDateString();
  }
}
