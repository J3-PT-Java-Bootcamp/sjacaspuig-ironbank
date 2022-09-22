import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountsRoutingModule } from './accounts-routing.module';
import { AccountsComponent } from './accounts.component';
import { LayoutTwoColumnsModule } from 'src/app/components/layout-two-columns/layout-two-columns.module';
import { CheckingAccountCreateModule } from 'src/app/components/checking-account-create/checking-account-create.module';
import { NbButtonModule, NbCardModule, NbIconModule, NbListModule, NbRouteTabsetModule, NbUserModule } from '@nebular/theme';
import { CheckingAccountCreationComponent } from './_components/checking-account-creation/checking-account-creation.component';
import { AccountListComponent } from './_components/account-list/account-list.component';


@NgModule({
  declarations: [
    AccountsComponent,
    CheckingAccountCreationComponent,
    AccountListComponent
  ],
  imports: [
    CommonModule,
    AccountsRoutingModule,
    LayoutTwoColumnsModule,
    CheckingAccountCreateModule,
    NbCardModule,
    NbListModule,
    NbUserModule,
    NbIconModule,
    NbButtonModule,
    NbRouteTabsetModule
  ]
})
export class AccountsModule { }
