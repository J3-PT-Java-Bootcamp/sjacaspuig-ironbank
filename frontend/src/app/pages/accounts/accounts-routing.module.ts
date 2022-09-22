import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountsComponent } from './accounts.component';
import { AccountListComponent } from './_components/account-list/account-list.component';
import { CheckingAccountCreationComponent } from './_components/checking-account-creation/checking-account-creation.component';

const routes: Routes = [
  {
    path: '',
    component: AccountsComponent,
    children: [
      {
        path: 'creation',
        component: CheckingAccountCreationComponent
      },
      {
        path: 'list',
        component: AccountListComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountsRoutingModule { }
