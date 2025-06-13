import { Routes } from '@angular/router';
import { MainInterfaceComponent } from './main-interface/main-interface.component';
import { NotFoundComponent } from './not-found/not-found.component';

export const routes: Routes = [
    {path: "", component: MainInterfaceComponent},
    {path: "**", component: NotFoundComponent}
];
