import { Component } from '@angular/core';
import { DesktopComponent } from './desktop/desktop.component';

@Component({
  selector: 'app-main-widget',
  imports: [DesktopComponent],
  template: ` <app-desktop class="hidden lg:block"> </app-desktop> `,
  styles: ``,
})
export class MainWidgetComponent {}
