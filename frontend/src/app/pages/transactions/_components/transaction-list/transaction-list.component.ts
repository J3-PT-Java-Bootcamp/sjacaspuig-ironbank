import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Transaction } from 'src/app/models/transaction';
import { TransactionService } from 'src/app/services/transaction.service';
import { tap } from 'rxjs/operators';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-transaction-list',
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.scss']
})
export class TransactionListComponent implements OnInit {

  public transactions$!: Observable<Transaction[]>;
  public spinner: boolean = false;
  public currentUserId: string = '';

  constructor(
    private transactionService: TransactionService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.currentUserId = this.authService.getUserId();
    this.getTransactions();
  }

  getTransactions() {
    this.spinner = true;
    this.transactions$ = this.transactionService.getAll().pipe(tap(() => this.spinner = false));
  }

}
