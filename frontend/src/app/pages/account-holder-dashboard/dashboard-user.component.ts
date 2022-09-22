import { Component, OnInit } from '@angular/core';
import { AccountHolder } from 'src/app/models/account-holder';
import { AccountHolderService } from 'src/app/services/account-holder.service';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { mergeMap } from 'rxjs/operators';
import { User } from 'src/app/models/user';
import { Transaction } from 'src/app/models/transaction';
import { TransactionService } from 'src/app/services/transaction.service';
import { tap } from 'rxjs/operators';
import { AccountService } from 'src/app/services/account.service';
import { Account } from 'src/app/models/account';
import { Money } from 'src/app/models/money';

@Component({
  selector: 'app-dashboard-user',
  templateUrl: './dashboard-user.component.html',
  styleUrls: ['./dashboard-user.component.scss']
})
export class DashboardUserComponent implements OnInit {

  accountHolder$!: Observable<AccountHolder>;
  spinnerUser: boolean = false;
  spinnerTransaction: boolean = false;
  spinnerAccount: boolean = false;
  transactions$!: Observable<Transaction[]>;
  accounts$!: Observable<Account[]>;
  totalBalance: Money = new Money('0', 'EUR');
  activeAccount: number = -1;

  constructor(
    private accountHolderService: AccountHolderService,
    private authService: AuthService,
    private transactionService: TransactionService,
    private accountService: AccountService
  ) { }

  ngOnInit(): void {
    this.getAccountHolder();
    this.getTransactionsByUserId();
    this.getAccountsByUserId();
  }

  getAccountHolder() {
    this.spinnerUser = true;
    this.accountHolder$ = this.authService.user$.pipe(
      mergeMap((user: User) => {
        return this.accountHolderService.getById(user.id)
      }),
      tap(() => this.spinnerUser = false)
    );
  }

  getTransactionsByUserId() {
    this.spinnerTransaction = true;
    this.transactions$ = this.authService.user$.pipe(
      mergeMap((user: User) => {
        return this.transactionService.findByAccountHolderId(user.id)
      }),
      tap(() => this.spinnerTransaction = false)
    );
  }

  getAccountsByUserId() {
    this.spinnerAccount = true;
    this.accounts$ = this.authService.user$.pipe(
      mergeMap((user: User) => {
        return this.accountService.getByUserId(user.id);
      }),
      tap((accounts: Account[]) => {
        accounts.forEach((account: Account) => {
          this.totalBalance.amount = (+this.totalBalance.amount + (+account.balance.amount)).toString();
        });
        this.spinnerAccount = false
      })
    );
  }

  showTransactionsByAccount(account: Account) {
    this.spinnerTransaction = true;
    if (account.iban) {
      this.transactions$ = this.transactionService.findByIban(account.iban).pipe(
        tap(() => this.spinnerTransaction = false)
      );
    } else {
      this.getTransactionsByUserId();
      this.spinnerTransaction = false;
    }
  }

}
