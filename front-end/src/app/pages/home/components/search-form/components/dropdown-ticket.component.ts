import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TicketCounter } from '../../../../../model/ticket_counter';

@Component({
  selector: 'app-dropdown-tickets',
  imports: [],
  template: `
    @if (showDropdown) {
    <div
      class="absolute z-50 w-[150%] mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-y-auto"
      (mousedown)="onMouseDown($event)"
    >
      <div class="flex justify-between px-6 py-2">
        <div>Adults</div>
        <div class="flex flex-row border-2 rounded-full p-1">
          <div class="cursor-pointer" (click)="decrementTickets('Adult')">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              class="size-7 rounded-full bg-custom-nav-via/80 hover:bg-custom-nav-via/70 p-1"
            >
              <path
                fill-rule="evenodd"
                d="M4.25 12a.75.75 0 0 1 .75-.75h14a.75.75 0 0 1 0 1.5H5a.75.75 0 0 1-.75-.75Z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
          <div class="px-6 w-16 text-center">
            {{ this.currentTicketsToGet.adults }}
          </div>
          <div class="cursor-pointer" (click)="incrementTickets('Adult')">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              class="size-7 rounded-full bg-custom-nav-via/80 hover:bg-custom-nav-via/70 p-1"
            >
              <path
                fill-rule="evenodd"
                d="M12 3.75a.75.75 0 0 1 .75.75v6.75h6.75a.75.75 0 0 1 0 1.5h-6.75v6.75a.75.75 0 0 1-1.5 0v-6.75H4.5a.75.75 0 0 1 0-1.5h6.75V4.5a.75.75 0 0 1 .75-.75Z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
        </div>
      </div>
      <div class="flex justify-between px-6 py-2">
        <div>Children</div>
        <div class="flex flex-row border-2 rounded-full p-1">
          <div class="cursor-pointer" (click)="decrementTickets('Child')">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              class="size-7 rounded-full bg-custom-nav-via/80 hover:bg-custom-nav-via/70 p-1"
            >
              <path
                fill-rule="evenodd"
                d="M4.25 12a.75.75 0 0 1 .75-.75h14a.75.75 0 0 1 0 1.5H5a.75.75 0 0 1-.75-.75Z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
          <div class="px-6 w-16 text-center">
            {{ this.currentTicketsToGet.children }}
          </div>
          <div class="cursor-pointer" (click)="incrementTickets('Child')">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              class="size-7 rounded-full bg-custom-nav-via/80 hover:bg-custom-nav-via/70 p-1"
            >
              <path
                fill-rule="evenodd"
                d="M12 3.75a.75.75 0 0 1 .75.75v6.75h6.75a.75.75 0 0 1 0 1.5h-6.75v6.75a.75.75 0 0 1-1.5 0v-6.75H4.5a.75.75 0 0 1 0-1.5h6.75V4.5a.75.75 0 0 1 .75-.75Z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
        </div>
      </div>
      <div class="flex justify-between px-6 py-2">
        <div>Infants</div>
        <div class="flex flex-row border-2 rounded-full p-1">
          <div class="cursor-pointer" (click)="decrementTickets('Infant')">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              class="size-7 rounded-full bg-custom-nav-via/80 hover:bg-custom-nav-via/70 p-1"
            >
              <path
                fill-rule="evenodd"
                d="M4.25 12a.75.75 0 0 1 .75-.75h14a.75.75 0 0 1 0 1.5H5a.75.75 0 0 1-.75-.75Z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
          <div class="px-6 w-16 text-center">
            {{ this.currentTicketsToGet.infants }}
          </div>
          <div class="cursor-pointer" (click)="incrementTickets('Infant')">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              class="size-7 rounded-full bg-custom-nav-via/80 hover:bg-custom-nav-via/70 p-1"
            >
              <path
                fill-rule="evenodd"
                d="M12 3.75a.75.75 0 0 1 .75.75v6.75h6.75a.75.75 0 0 1 0 1.5h-6.75v6.75a.75.75 0 0 1-1.5 0v-6.75H4.5a.75.75 0 0 1 0-1.5h6.75V4.5a.75.75 0 0 1 .75-.75Z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
        </div>
      </div>
    </div>
    }
  `,
  styles: ``,
})
export class DropdownTicketComponent {
  @Input() showDropdown = false;

  @Output() peopleNumberChanged = new EventEmitter<TicketCounter>();

  currentTicketsToGet: TicketCounter = { adults: 0, children: 0, infants: 0 };

  incrementTickets(type: 'Adult' | 'Child' | 'Infant') {
    if (type === 'Adult') {
      this.currentTicketsToGet.adults++;
    } else if (type === 'Child') {
      this.currentTicketsToGet.children++;
    } else if (type === 'Infant') {
      this.currentTicketsToGet.infants++;
    }

    this.peopleNumberChanged.emit(this.currentTicketsToGet);
  }

  decrementTickets(type: 'Adult' | 'Child' | 'Infant') {
    let hasChanged = false;

    if (type === 'Adult' && this.currentTicketsToGet.adults > 0) {
      this.currentTicketsToGet.adults--;
      hasChanged = true;
    } else if (type === 'Child' && this.currentTicketsToGet.children > 0) {
      this.currentTicketsToGet.children--;
      hasChanged = true;
    } else if (type === 'Infant' && this.currentTicketsToGet.infants > 0) {
      this.currentTicketsToGet.infants--;
      hasChanged = true;
    }

    if (hasChanged) {
      this.peopleNumberChanged.emit(this.currentTicketsToGet);
    }
  }

  onMouseDown(event: MouseEvent) {
    event.preventDefault();
  }

  select() {
    this.showDropdown = false;
  }
}
