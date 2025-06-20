import { Routes } from '@angular/router';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { HomeComponent } from './pages/home/home.component';
import { AdminManageFlightsComponent } from './pages/admin-manage-flights/admin-manage-flights.component';

export const routes: Routes = [
    {path: "", component: HomeComponent},
    {path: "flights", component:AdminManageFlightsComponent},
    {path: "**", component: NotFoundComponent}
];
