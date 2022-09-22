import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AccountHolder } from '../models/account-holder';

@Injectable({
  providedIn: 'root'
})
export class AccountHolderService {

  userURL = environment.apiUrl + '/account-holders';
  httpOptions = { headers: new HttpHeaders({'Content-Type' : 'application/json'})};

  constructor(
    private httpClient: HttpClient
  ) {}

  public create(accountHolder: AccountHolder): Observable<AccountHolder> {
    return this.httpClient.post<AccountHolder>(this.userURL, accountHolder, this.httpOptions).pipe(map((accountHolder: AccountHolder) => this.formatDateToUTC(accountHolder)));
  }

  public getAll(): Observable<AccountHolder[]> {
    return this.httpClient.get<AccountHolder[]>(this.userURL).pipe(map((accountHolders: AccountHolder[]) => this.formatDatesToUTC(accountHolders)));
  }

  public deleteAccountHolder(id: string): Observable<AccountHolder> {
    return this.httpClient.delete<AccountHolder>(this.userURL + '/' + id, this.httpOptions).pipe(map((accountHolder: AccountHolder) => this.formatDateToUTC(accountHolder)));
  }

  public getById(id: string): Observable<AccountHolder> {
    return this.httpClient.get<AccountHolder>(this.userURL + '/' + id).pipe(map((accountHolder: AccountHolder) => this.formatDateToUTC(accountHolder)));
  }

  private formatDatesToUTC(accountHolders: AccountHolder[]): AccountHolder[] {
    return accountHolders.map((accountHolder: AccountHolder) => this.formatDateToUTC(accountHolder));
  }

  private formatDateToUTC(accountHolder: AccountHolder): AccountHolder { 
    if (accountHolder?.birthDate) {
      accountHolder.birthDate = new Date(new Date(accountHolder.birthDate).toUTCString());
    }
    return accountHolder;
  }
}
