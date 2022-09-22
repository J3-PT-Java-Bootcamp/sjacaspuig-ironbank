import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TransactionsRoutingModule } from './transactions-routing.module';
import { TransactionsComponent } from './transactions.component';
import { LayoutTwoColumnsModule } from 'src/app/components/layout-two-columns/layout-two-columns.module';
import { NbAccordionModule, NbButtonModule, NbCardModule, NbIconModule, NbListModule, NbRouteTabsetModule, NbSpinnerModule, NbTabsetModule, NbUserModule } from '@nebular/theme';
import { TransactionCreateModule } from 'src/app/components/transaction-create/transaction-create.module';
import { TransactionListComponent } from './_components/transaction-list/transaction-list.component';
import { TransactionCreationComponent } from './_components/transaction-creation/transaction-creation.component';


@NgModule({
  declarations: [
    TransactionsComponent,
    TransactionListComponent,
    TransactionCreationComponent
  ],
  imports: [
    CommonModule,
    TransactionsRoutingModule,
    LayoutTwoColumnsModule,
    NbCardModule,
    NbUserModule,
    NbListModule,
    NbButtonModule,
    NbIconModule,
    TransactionCreateModule,
    NbTabsetModule,
    NbRouteTabsetModule,
    NbSpinnerModule,
    NbAccordionModule
  ]
})
export class TransactionsModule { }
