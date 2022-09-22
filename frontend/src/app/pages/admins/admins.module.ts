import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminsRoutingModule } from './admins-routing.module';
import { AdminsComponent } from './admins.component';
import { AdminCreateModule } from 'src/app/components/admin-create/admin-create.module';
import { LayoutTwoColumnsModule } from 'src/app/components/layout-two-columns/layout-two-columns.module';
import { NbButtonModule, NbCardModule, NbIconModule, NbListModule, NbUserModule } from '@nebular/theme';


@NgModule({
  declarations: [
    AdminsComponent
  ],
  imports: [
    CommonModule,
    AdminsRoutingModule,
    AdminCreateModule,
    LayoutTwoColumnsModule,
    NbCardModule,
    NbUserModule,
    NbListModule,
    NbButtonModule,
    NbIconModule
  ]
})
export class AdminsModule { }
