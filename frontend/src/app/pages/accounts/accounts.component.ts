import { Component } from '@angular/core';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent {

  tabs: any[] = [
    {
      title: 'Create Checking Account',
      route: '/accounts/creation',
      icon: 'file-add-outline',
      responsive: true
    },
    {
      title: 'Accounts List',
      route: '/accounts/list',
      icon: 'folder-outline',
      responsive: true
    },
  ];
}
