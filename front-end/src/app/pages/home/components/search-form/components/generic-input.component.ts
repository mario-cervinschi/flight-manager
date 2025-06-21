import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Airport } from '../../../../../model/airport';
import { CalendarDay } from '../../../../../model/calendar_day';

@Component({
  selector: 'app-generic-input',
  imports: [],
  template: `<label
      class="absolute pl-3 pt-1 text-sm font-medium text-custom-nav-via/80 mb-2 z-10 transition-all duration-200"
      [class.opacity-100]="searchTerm || currentObject"
      [class.opacity-0]="!searchTerm && !currentObject"
      [class.pointer-events-none]="!searchTerm && !currentObject"
    >
      {{ searchTerm || currentObject ? inputLabel : '' }}
    </label>

    <div class="relative">
      <input
        type="text"
        class="w-full px-4 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 bg-gray-50 transition-all placeholder-custom-nav-primary/50 text-custom-nav-via/90 font-semibold text-lg"
        [placeholder]="
          inputType === 'Airport' && currentObject
            ? currentObject.city + ' (' + currentObject.code + ')'
            : inputType === 'Calendar' && currentObject
            ? currentObject.date +
              ' ' +
              currentObject.month +
              ' ' +
              currentObject.year
            : inputLabel
        "
        [value]="
          inputType === 'Airport' && currentObject
            ? currentObject.city + ' (' + currentObject.code + ')'
            : inputType === 'Calendar' && currentObject
            ? currentObject.date +
              ' ' +
              currentObject.month +
              ' ' +
              currentObject.year
            : searchTerm
        "
        [class.pt-5]="searchTerm || currentObject"
        [class.pb-1]="searchTerm || currentObject"
        [class.py-3.5]="!searchTerm && !currentObject"
        [class.text-gray-400]="!currentObject"
        (input)="onSearch($event)"
        (keydown)="onKeyDown($event)"
        (focus)="onFocus()"
        (blur)="onBlur()"
      />
    </div>`,
  styles: ``,
})
export class GenericInputComponent {
  @Input() inputLabel: string = '';
  @Input() inputType: 'Airport' | 'Calendar' = 'Airport';
  @Input() currentObject: any = null;

  @Output() inputSearchTermChange = new EventEmitter<string>();
  @Output() hideCurrentDropdown = new EventEmitter<boolean>();

  searchTerm = '';

  select(object: CalendarDay | Airport) {
    this.currentObject = object;
    this.searchTerm = '';
  }

  onSearch(event: any) {
    if (event.target.value.toLowerCase().length > 0) {
      this.searchTerm = event.target.value.toLowerCase();
    } else{
        this.searchTerm = '';
    }

    this.inputSearchTermChange.emit(event);
  }

  onKeyDown(event: KeyboardEvent) {
    const isPrintableKey = event.key.length === 1;
    const isDeletionKey = event.key === 'Backspace' || event.key === 'Delete';

    if (this.currentObject && (isPrintableKey || isDeletionKey)) {
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
    this.currentObject = null;
    this.searchTerm = '';
  }

  onFocus() {
    this.hideCurrentDropdown.emit(false);
    console.log('activated select');
  }

  onBlur() {
    setTimeout(() => {
      this.hideCurrentDropdown.emit(true);
      if (!this.currentObject && !this.searchTerm) {
        this.searchTerm = '';
      }
    }, 100);
  }
}
