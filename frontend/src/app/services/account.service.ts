import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { map, Observable } from 'rxjs';
import { Account } from '../models/account';
import { AccountStatus } from '../types/account-status';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  userURL = environment.apiUrl + '/accounts';
  httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

  constructor(
    private httpClient: HttpClient
  ) { }

  public getByIban(iban: string): Observable<Account> {
    return this.httpClient.get<any>(this.userURL + '/' + iban, this.httpOptions).pipe(map((account: Account) => this.formatDateToUTC(account)));
  }

  public getAll(): Observable<Account[]> {
    return this.httpClient.get<Account[]>(this.userURL, this.httpOptions).pipe(map((accounts: Account[]) => this.formatDatesToUTC(accounts)));
  }

  public deleteAccount(iban: string): Observable<Account> {
    return this.httpClient.delete<Account>(this.userURL + '/' + iban, this.httpOptions).pipe(map((account: Account) => this.formatDateToUTC(account)));
  }

  public changeStatus(account: { iban: string, status: AccountStatus }): Observable<Account> {
    return this.httpClient.patch<Account>(this.userURL + '/change-status/' + account.iban, account, this.httpOptions).pipe(map((account: Account) => this.formatDateToUTC(account)));
  }

  public getByUserId(accountHolderId: string): Observable<Account[]> {
    return this.httpClient.get<Account[]>(this.userURL + '/user/' + accountHolderId, this.httpOptions).pipe(map((accounts: Account[]) => this.formatDatesToUTC(accounts)));
  }

  private formatDatesToUTC(accounts: Account[]): Account[] {
    return accounts.map((account: Account) => this.formatDateToUTC(account));
  }

  private formatDateToUTC(account: Account): Account { 
    if (account?.creationDate) {
      account.creationDate = new Date(new Date(account.creationDate).toUTCString());
    }
    if (account?.interestRateDate) {
      account.interestRateDate = new Date(new Date(account.interestRateDate).toUTCString());
    }
    if (account?.lastMonthlyFeeDate) {
      account.lastMonthlyFeeDate = new Date(new Date(account.lastMonthlyFeeDate).toUTCString());
    }
    return account;
  }
}
