import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { CalendarDay } from '../../../../../model/calendar_day';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-dropdown-calendar',
  imports: [NgClass],
  template:`@if (showDropdown) {
    <div class="absolute z-50 left-0 right-0 mt-1 bg-white border border-gray-300 rounded-md shadow-lg overflow-y-auto" (mousedown)="onMouseDown($event)">
      <div
        class="flex items-center justify-between mb-4 bg-custom-nav-via/80 text-white/80 select-none"
      >
        <button
          type="button"
          class="text-2xl border-none cursor-pointer px-3 pb-1 my-1.5 ml-1 rounded-md hover:bg-custom-nav-secondary/60"
          (click)="previousMonth()"
        >
          ‹
        </button>
        <span class="month-year pb-1">
          {{ monthNames[currentMonth] }} {{ currentYear }}
        </span>
        <button
          type="button"
          class="text-2xl border-none cursor-pointer px-3 pb-1 my-1.5 mr-1 rounded-md hover:bg-custom-nav-secondary/60"
          (click)="nextMonth()"
        >
          ›
        </button>
      </div>
    
      <!-- WeekDays -->
      <div class="grid grid-cols-7 -mt-2 mb-2 select-none">
        @for (day of weekDays; track day) {
          <div class="text-center">{{ day }}</div>
        }
      </div>
    
      <!-- MonthDays -->
      <div class="grid grid-cols-7 gap-x-2 mx-1 mb-1">
        @for (week of getWeeks(); track $index) {
          @for (day of week; track day.date) {
            <div
              class="h-12 flex flex-col items-center justify-center relative font-semibold"
              [ngClass]="{
                'cursor-pointer text-custom-nav-secondary/70 hover:bg-custom-nav-secondary/20 hover:rounded-md': day.enabled,
                'text-custom-nav-primary/10': !isCurrentMonth(day.date),
                'text-custom-nav-primary/40 cursor-default': !day.enabled && isCurrentMonth(day.date),
                'today': isToday(day.date)
              }"
              (mousedown)="selectDate(day)"
            >
              <span class="mb-2">{{ day.date.getDate() }}</span>
              @if (day.price && day.enabled) {
                <span class="absolute text-3xs mt-5">
                  {{ day.price }}RON
                </span>
              }
            </div>
          }
        }
      </div>
    </div>
  }`,
  styles: ``,
})
export class DropdownCalendarComponent implements OnInit {
  @Input() showDropdown = false;
  @Input() availableDates: CalendarDay[] = [];

  @Output() dateSelected = new EventEmitter<Date>();
  @Output() monthChanged = new EventEmitter<{ year: number; month: number }>();

  currentDate = new Date();
  currentMonth = new Date().getMonth();
  currentYear = new Date().getFullYear();
  weekDays = ['L', 'Ma', 'Mi', 'J', 'V', 'S', 'D'];
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

    const firstDayOfWeek = (firstDay.getDay() + 6) % 7;
    startDate.setDate(startDate.getDate() - firstDayOfWeek);

    this.calendarDays = [];

    for (let i = 0; i < 42; i++) {
      // 6 weeks x 7 days
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

  getWeeks() {
    const weeks = [];
    for (let i = 0; i < this.calendarDays.length; i += 7) {
      const week = this.calendarDays.slice(i, i + 7);
      weeks.push(week);
    }
  
    const lastWeek = weeks[weeks.length - 1];
    const allOutOfMonth = lastWeek.every(day => !this.isCurrentMonth(day.date));
  
    if (allOutOfMonth) {
      weeks.pop(); 
    }
  
    return weeks;
  }
  

  onMouseDown(event: MouseEvent) {
    event.preventDefault();
  }

  selectDate(day: CalendarDay) {
    if (day.enabled) {
      this.inputValue = day.date.toLocaleDateString('ro-RO');
      this.dateSelected.emit(day.date);
      this.showDropdown = false;
    }
  }

  private requestMonthData() {
    this.monthChanged.emit({
      year: this.currentYear,
      month: this.currentMonth + 1,
    });
  }

  previousMonth() {
    if (this.currentMonth === 0) {
      this.currentMonth = 11;
      this.currentYear--;
    } else {
      this.currentMonth--;
    }
    this.requestMonthData();
  }

  nextMonth() {
    if (this.currentMonth === 11) {
      this.currentMonth = 0;
      this.currentYear++;
    } else {
      this.currentMonth++;
    }
    this.requestMonthData();
  }

  isCurrentMonth(date: Date): boolean {
    return date.getMonth() === this.currentMonth;
  }

  isToday(date: Date): boolean {
    const today = new Date();
    return date.toDateString() === today.toDateString();
  }
}