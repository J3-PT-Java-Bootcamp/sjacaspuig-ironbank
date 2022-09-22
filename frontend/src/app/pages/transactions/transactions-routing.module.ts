import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TransactionsComponent } from './transactions.component';
import { TransactionCreationComponent } from './_components/transaction-creation/transaction-creation.component';
import { TransactionListComponent } from './_components/transaction-list/transaction-list.component';

const routes: Routes = [
  {
    path: '',
    component: TransactionsComponent,
    children: [
      {
        path: 'creation',
        component: TransactionCreationComponent
      },
      {
        path: 'list',
        component: TransactionListComponent
      }
    ],
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransactionsRoutingModule { }
