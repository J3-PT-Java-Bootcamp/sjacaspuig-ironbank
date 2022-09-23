import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Admin } from '../models/admin';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  userURL = environment.apiUrl + '/admins';
  httpOptions = { headers: new HttpHeaders({
    'Content-Type' : 'application/json'
  })};

  constructor(
    private httpClient: HttpClient
  ) {}

  public create(admin: Admin): Observable<Admin> {
    return this.httpClient.post<Admin>(this.userURL, admin, this.httpOptions);
  }

  public getAll(): Observable<Admin[]> {
    return this.httpClient.get<Admin[]>(this.userURL, this.httpOptions);
  }

  public deleteAdmin(id: string): Observable<void> {
    return this.httpClient.delete<void>(this.userURL + '/' + id, this.httpOptions);
  }
}
