import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {OverviewComponent} from "../page/overview/overview.component";
import {DetailsComponent} from "../page/details/details.component";

const routes: Routes = [
  { path: 'overview', component: OverviewComponent },
  { path: 'details', component: DetailsComponent },
  { path: '',   redirectTo: '/overview', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
