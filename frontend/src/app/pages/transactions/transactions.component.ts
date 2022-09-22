import { Component } from '@angular/core';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.scss']
})
export class TransactionsComponent {

  tabs: any[] = [
    {
      title: 'Create Transaction',
      route: '/transactions/creation',
      icon: 'file-add-outline',
      responsive: true
    },
    {
      title: 'Transactions List',
      route: '/transactions/list',
      icon: 'folder-outline',
      responsive: true
    },
  ];
}
