import { Component, OnInit } from '@angular/core';
import { AccountHolder } from 'src/app/models/account-holder';
import { AccountHolderService } from 'src/app/services/account-holder.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-checking-account-creation',
  templateUrl: './checking-account-creation.component.html',
  styleUrls: ['./checking-account-creation.component.scss']
})
export class CheckingAccountCreationComponent implements OnInit {

  public accountHolders$!: Observable<AccountHolder[]>;

  constructor(
    private accountHolderService: AccountHolderService
  ) { }

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers() {
    this.accountHolders$ = this.accountHolderService.getAll();
  }
}
