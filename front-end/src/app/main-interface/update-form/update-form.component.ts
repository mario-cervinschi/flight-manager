import { CommonModule } from '@angular/common';
import { Component, Input, ViewChild } from '@angular/core';
import { Flight } from '../../model/flight';
import { FormInputComponent } from "../form-input/form-input.component";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ServicesService } from '../../services.service';

declare var bootstrap: any;

@Component({
  selector: 'app-update-form',
  imports: [CommonModule, ReactiveFormsModule, FormInputComponent],
  templateUrl: './update-form.component.html',
  styleUrl: './update-form.component.css'
})
export class UpdateFormComponent {
  @Input() flight!: Flight;
  @ViewChild(FormInputComponent) formInput!: FormInputComponent;

  fg = new FormGroup({
    destinationForm: new FormControl('', Validators.required),
    airportNameForm: new FormControl('', Validators.required),
    dateForm: new FormControl('', Validators.required),
    seatNumbersForm: new FormControl('', [Validators.required, Validators.min(1)])
  })

  visible = false;

  constructor(private service: ServicesService){}

  openModal(): void {
    
    this.visible = true;
  }

  close(): void {
    this.visible = false;
  }

  submitUpdate(): void {
    if(this.formInput.handleUpdate(this.flight)){
      this.close();
    }
  }
}
