import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarDay } from '../../../../../model/calendar_day';

@Component({
  selector: 'app-dropdown-calendar',
  imports: [CommonModule],
  templateUrl:'dropdown.html',
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
    event.preventDefault(); // Prevents input from losing focus
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