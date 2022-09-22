import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardUserComponent } from './dashboard-user.component';
import { DashboardUserRoutingModule } from './dashboard-user-routing.module';
import { LayoutTwoColumnsModule } from 'src/app/components/layout-two-columns/layout-two-columns.module';
import { NbAccordionModule, NbCardModule, NbIconModule, NbListModule, NbSpinnerModule } from '@nebular/theme';



@NgModule({
  declarations: [
    DashboardUserComponent
  ],
  imports: [
    CommonModule,
    DashboardUserRoutingModule,
    LayoutTwoColumnsModule,
    NbCardModule,
    NbListModule,
    NbAccordionModule,
    NbSpinnerModule,
    NbIconModule
  ]
})
export class DashboardUserModule { }
