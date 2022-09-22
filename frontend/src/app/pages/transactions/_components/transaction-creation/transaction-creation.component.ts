import { Component, OnInit } from '@angular/core';
import { AccountHolder } from 'src/app/models/account-holder';
import { AccountHolderService } from 'src/app/services/account-holder.service';
import { Observable } from 'rxjs';
import { AccountService } from 'src/app/services/account.service';
import { Account } from 'src/app/models/account';
import { ThirdPartyService } from 'src/app/services/third-party.service';
import { ThirdParty } from 'src/app/models/third-party';

@Component({
  selector: 'app-transaction-creation',
  templateUrl: './transaction-creation.component.html',
  styleUrls: ['./transaction-creation.component.scss']
})
export class TransactionCreationComponent implements OnInit {

  public transactions$!: Observable<AccountHolder[]>;
  public accounts$!: Observable<Account[]>;
  public thirdParties$!: Observable<ThirdParty[]>;

  constructor(
    private thirdPartyService: ThirdPartyService,
    private accountHolderService: AccountHolderService,
    private accountService: AccountService
  ) { }

  ngOnInit(): void {
    this.getTransactions();
    this.getAccounts();
    this.getThirdParties();
  }

  deleteTransaction(id: string | undefined): void {
    if (confirm("Are you sure?") && id) {
      this.accountHolderService.deleteAccountHolder(id).subscribe(() => this.getTransactions());
    }
  }

  getTransactions() {
    this.transactions$ = this.accountHolderService.getAll();
  }

  getAccounts() {
    this.accounts$ = this.accountService.getAll();
  }

  getThirdParties() {
    this.thirdParties$ = this.thirdPartyService.getAll();
  }
}
