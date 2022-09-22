import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountHolder } from 'src/app/models/account-holder';
import { AccountHolderService } from 'src/app/services/account-holder.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  public accountHolders$!: Observable<AccountHolder[]>;

  constructor(
    private accountHolderService: AccountHolderService
  ) { }

  ngOnInit(): void {
    this.getUsers();
  }

  deleteUser(id: string | undefined): void {
    if (confirm("Are you sure?") && id) {
      this.accountHolderService.deleteAccountHolder(id).subscribe(() => this.getUsers());
    }
  }

  getUsers() {
    this.accountHolders$ = this.accountHolderService.getAll();
  }
}
