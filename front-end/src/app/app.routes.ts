import { Routes } from '@angular/router';
import { MainInterfaceComponent } from './main-interface/main-interface.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { ManageFlightsInterfaceComponent } from './manage-flights-interface/manage-flights-interface.component';

export const routes: Routes = [
    {path: "", component: MainInterfaceComponent},
    {path: "flights", component: ManageFlightsInterfaceComponent},
    {path: "**", component: NotFoundComponent}
];
