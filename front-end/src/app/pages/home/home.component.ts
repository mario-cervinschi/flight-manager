import { Component } from '@angular/core';
import { MainWidgetComponent } from './components/main-widget/main-widget.component';

@Component({
  selector: 'app-home',
  imports: [MainWidgetComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
