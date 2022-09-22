import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { CheckingAccount } from '../models/checking-account';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CheckingAccountService {

  userURL = environment.apiUrl + '/checking-accounts';
  httpOptions = { headers: new HttpHeaders({'Content-Type' : 'application/json'})};

  constructor(
    private httpClient: HttpClient
  ) {}

  public create(checkingAccount: CheckingAccount): Observable<CheckingAccount> {
    return this.httpClient.post<CheckingAccount>(this.userURL, checkingAccount, this.httpOptions);
  }

  public getAll(): Observable<CheckingAccount[]> {
    return this.httpClient.get<CheckingAccount[]>(this.userURL);
  }
}
