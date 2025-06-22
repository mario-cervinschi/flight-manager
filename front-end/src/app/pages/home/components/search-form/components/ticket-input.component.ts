import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { GenericInputComponent } from './generic-input.component';
import { DropdownTicketComponent } from './dropdown-ticket.component';
import { TicketCounter } from '../../../../../model/ticket_counter';

@Component({
  selector: 'app-ticket-input',
  imports: [GenericInputComponent, DropdownTicketComponent],
  template: `<app-generic-input
      [inputLabel]="inputLabel"
      [inputType]="'Ticket'"
      [currentObject]="currentTicketNumbers"
      (hideCurrentDropdown)="activeDropdown($event)"
    ></app-generic-input>

    <app-dropdown-tickets
      [showDropdown]="showTicketDropdown"
      (peopleNumberChanged)="onTicketNumberChange($event)"
    ></app-dropdown-tickets>`,
  styles: ``,
})
export class TicketInputComponent {
  @Input() inputLabel: string = 'NaN';

  @Output() peopleNumberChanged = new EventEmitter<TicketCounter>(); 

  showTicketDropdown: boolean = false;
  currentTicketNumbers: TicketCounter | null = null;

  onTicketNumberChange(event: any){
    this.currentTicketNumbers = event;
    this.peopleNumberChanged.emit(event);
  }

  activeDropdown(value: boolean) {
    this.showTicketDropdown = !value;
    // if(this.searchWords.length === 0){
    //   this.filteredAirports = this.airports;
    // }
  }
}
