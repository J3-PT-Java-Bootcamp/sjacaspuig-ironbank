import { Component, OnInit } from '@angular/core';
import { Account } from 'src/app/models/account';
import { Observable } from 'rxjs';
import { AccountService } from 'src/app/services/account.service';
import { AccountStatus } from 'src/app/types/account-status';

@Component({
  selector: 'app-account-list',
  templateUrl: './account-list.component.html',
  styleUrls: ['./account-list.component.scss']
})
export class AccountListComponent implements OnInit {

  public accounts$!: Observable<Account[]>;

  constructor(
    private accountService: AccountService
  ) { }

  ngOnInit(): void {
    this.getAccounts();
  }

  getAccounts() {
    this.accounts$ = this.accountService.getAll();
  }

  changeStatus(account: Account): void {
    if (confirm("Are you sure?") && account.iban && account.status) {
      const status: AccountStatus = account.status === 'ACTIVE' ? 'FROZEN' : 'ACTIVE';
      const accountBody = { iban: account.iban, status: status };
      this.accountService.changeStatus(accountBody).subscribe(() => this.getAccounts());
    }
  }
}
