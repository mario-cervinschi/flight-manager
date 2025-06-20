import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ServicesService } from '../../../shared/services.service';

@Component({
  selector: 'app-form-input',
  imports: [ReactiveFormsModule],
  templateUrl: './form-input.component.html',
  styleUrl: './form-input.component.css'
})
export class FormInputComponent {
  fg = new FormGroup({
    destinationForm: new FormControl('', Validators.required),
    airportNameForm: new FormControl('', Validators.required),
    dateForm: new FormControl('', Validators.required),
    seatNumbersForm: new FormControl('', [Validators.required, Validators.min(1)])
  })

  constructor(private service: ServicesService){}

  retrieveFormData() : FormGroup | null{
    this.fg.markAllAsTouched();

    if(this.fg.valid){
      return this.fg;
    }else{
      return null;
    }
  }
}
