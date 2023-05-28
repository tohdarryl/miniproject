import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { lastValueFrom, map, Observable } from 'rxjs';
import { Account } from '../models/account';

import { JwtHelperService } from '@auth0/angular-jwt';
@Injectable({
  providedIn: 'root'
})
export class AccountService {


  helper = new JwtHelperService();
  decodedToken: any;

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) { }

  // View 'Register': To create account
  // createAccount(a: Account): Promise<any> {
  //   // const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
  //   const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  //   const body = JSON.stringify(a);
  //   console.log("create new account !");
  //   return lastValueFrom(this.http.post<Account>('register', body, { headers: headers }));
  // }

  // View 'Login': To retrieve account
  // retrieveAccount(email: String, password: string): Promise<any> {
  //   const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
  //   console.log("retrieve account details for: " + email);
  //   return lastValueFrom(this.http.get<Account>('login/' + email + ' ' + password, { headers: headers }));
  // }

  getUsers(query: String) : Promise<any>{
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    console.log("get users for: "+query);
    return lastValueFrom(this.http.get<Account[]>('/auth/' + 'users/' + query, {headers: headers}));
  }

  signin(a: Account): Promise<any> {
    const body = JSON.stringify(a);
    return lastValueFrom(this.http.post<any>('/auth/' + 'signin', body, { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }).pipe(map((resp) => {
      localStorage.setItem('user', a.email);
      localStorage.setItem('token', resp.token);
      this.decodedToken = this.helper.decodeToken(resp.token);
      console.log(this.decodedToken)
      return resp;
    })));
  }

  signup(a: Account): Promise<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', responseType: 'text' as 'json' });
    return lastValueFrom(this.http.post<any>('/auth/' + 'signup', a, { headers: headers }));
  }

  changePassword(a: {}): Promise<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', responseType: 'text' as 'json' });
    const body = JSON.stringify(a);
    return lastValueFrom(this.http.post<any>('/auth/' + 'changePassword', body, { headers: headers }));
  }

  forgotPassword(a: {}): Promise<any>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', responseType: 'text' as 'json' });
    const body = JSON.stringify(a);
    return lastValueFrom(this.http.post<any>('/auth/' + 'forgotPassword', body, { headers: headers }));
  }

  signout() {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    this.router.navigate(['/login'])
  }

  isUserSignedin() {
    return localStorage.getItem('token') !== null;
  }

  getSignedinUser() {
    return localStorage.getItem('user') as string;
  }

  getToken() {
    let token = localStorage.getItem('token') as string;
    return token;
  }

  isTokenExpired() {
    const token = this.getToken();
    return this.helper.isTokenExpired(token);
  }
}
