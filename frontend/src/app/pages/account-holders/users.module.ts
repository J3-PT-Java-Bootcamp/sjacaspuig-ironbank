import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UsersRoutingModule } from './users-routing.module';
import { UsersComponent } from './users.component';
import { LayoutTwoColumnsModule } from 'src/app/components/layout-two-columns/layout-two-columns.module';
import { NbButtonModule, NbCardModule, NbIconModule, NbListModule, NbUserModule } from '@nebular/theme';
import { AccountHolderCreateModule } from 'src/app/components/account-holder-create/account-holder-create.module';


@NgModule({
  declarations: [
    UsersComponent
  ],
  imports: [
    CommonModule,
    UsersRoutingModule,
    LayoutTwoColumnsModule,
    NbCardModule,
    NbUserModule,
    NbListModule,
    NbButtonModule,
    NbIconModule,
    AccountHolderCreateModule
  ]
})
export class UsersModule { }
