import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { Airport } from '../../../../../model/airport';

@Component({
  selector: 'app-generic-input',
  imports: [],
  template: `<label
      class="absolute pl-3 pt-1 text-sm font-medium text-custom-nav-via/80 mb-2 z-10 transition-all duration-200 select-none"
      [class.opacity-100]="shouldShowLabel()"
      [class.opacity-0]="!shouldShowLabel()"
      [class.pointer-events-none]="!shouldShowLabel()"
    >
      {{ shouldShowLabel() ? inputLabel : '' }}
    </label>

    <div class="relative">
      <input
        #inputElement
        type="text"
        class="w-full px-4 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 bg-gray-50 transition-all placeholder-custom-nav-primary/50 text-custom-nav-via/90 font-semibold text-lg"
        [placeholder]="getPlaceholder()"
        [value]="getInputValue()"
        [class.pt-5]="shouldShowLabel()"
        [class.pb-1]="shouldShowLabel()"
        [class.py-3.5]="!shouldShowLabel()"
        [class.text-gray-400]="!currentObject"
        (input)="onSearch($event)"
        (keydown)="onKeyDown($event)"
        (focus)="onFocus()"
        (blur)="onBlur()"
      />

      <div
        class="absolute top-0.5 right-2 text-end text-xs font-medium text-custom-nav-via/60 pb-1 select-none pointer-events-none transition-all duration-100"
        [class.opacity-100]="hasAllThreeCategories()"
        [class.opacity-0]="!hasAllThreeCategories()"
      >
        A-Adults <br> C-Children <br> I-Infants
      </div>
    </div>`,
  styles: ``,
})
export class GenericInputComponent {
  @ViewChild('inputElement') inputElemetRef!: ElementRef<HTMLInputElement>;

  focusInput(){
    this.inputElemetRef?.nativeElement?.focus();
  }

  @Input() inputLabel: string = '';
  @Input() inputType: 'Airport' | 'Calendar' | 'Ticket' = 'Airport';
  @Input() currentObject: any = null;

  @Output() inputSearchTermChange = new EventEmitter<string>();
  @Output() hideCurrentDropdown = new EventEmitter<boolean>();

  searchTerm = '';

  hasAllThreeCategories(): boolean {
    return this.currentObject && 
           this.currentObject.adults > 0 && 
           this.currentObject.children > 0 && 
           this.currentObject.infants > 0;
  }

  shouldShowLabel(): boolean {
    if (this.inputType === 'Ticket') {
      return !!(this.searchTerm || (this.currentObject && this.hasTickets()));
    }
    return !!(this.searchTerm || this.currentObject);
  }

  hasTickets(): boolean {
    if (!this.currentObject) return false;
    return (
      this.currentObject.adults > 0 ||
      this.currentObject.children > 0 ||
      this.currentObject.infants > 0
    );
  }

  getPlaceholder(): string {
    if (this.inputType === 'Airport' && this.currentObject) {
      return this.currentObject.city + ' (' + this.currentObject.code + ')';
    }
    if (this.inputType === 'Calendar' && this.currentObject) {
      return this.formatDate(this.currentObject);
    }
    if (
      this.inputType === 'Ticket' &&
      this.currentObject &&
      this.hasTickets()
    ) {
      return this.getTicketDisplayText(this.currentObject);
    }
    return this.inputLabel;
  }

  getInputValue(): string {
    if (this.inputType === 'Airport' && this.currentObject) {
      return this.currentObject.city + ' (' + this.currentObject.code + ')';
    }
    if (this.inputType === 'Calendar' && this.currentObject) {
      return this.formatDate(this.currentObject);
    }
    if (
      this.inputType === 'Ticket' &&
      this.currentObject &&
      this.hasTickets()
    ) {
      return this.getTicketDisplayText(this.currentObject);
    }
    return this.searchTerm;
  }

  select(object: Date | Airport) {
    this.currentObject = object;
    this.searchTerm = '';
  }

  onSearch(event: any) {
    if (event.target.value.toLowerCase().length > 0) {
      this.searchTerm = event.target.value.toLowerCase();
    } else {
      this.searchTerm = '';
    }

    this.inputSearchTermChange.emit(event);
  }

  formatDate(date: Date | null): string {
    if (!date) return '';

    return date
      .toLocaleDateString('ro-RO', {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
      })
      .replace('.', '');
  }

  getTicketDisplayText(ticketObject: any): string {
    const parts: string[] = [];

    const hasAllCategories =
      ticketObject.adults > 0 &&
      ticketObject.children > 0 &&
      ticketObject.infants > 0;

    if (hasAllCategories) {
      parts.push(ticketObject.adults + ' A');
      parts.push(ticketObject.children + ' C');
      parts.push(ticketObject.infants + ' I');
    } else {
      if (ticketObject.adults > 0) {
        parts.push(ticketObject.adults + ' Adults');
      }
      if (ticketObject.children > 0) {
        parts.push(ticketObject.children + ' Children');
      }
      if (ticketObject.infants > 0) {
        parts.push(ticketObject.infants + ' Infants');
      }
    }

    return parts.join(', ');
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
    }, 25);
  }
}
