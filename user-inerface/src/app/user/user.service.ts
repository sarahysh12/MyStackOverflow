import { Injectable } from '@angular/core';
import { User } from "./user";
import { Http, Response, RequestOptions,URLSearchParams, Headers} from "@angular/http";
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import { Observable } from "rxjs/Observable";
import { CookieService } from 'ngx-cookie-service';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from "./auth.service";

@Injectable()
export class UserService {

private apiUrl = 'http://localhost:8080/users';

constructor(private http: Http,
            private route: ActivatedRoute,
            private router: Router,
            private authService: AuthService) {
  }

  findAll(): Observable<User[]>  {
    return this.http.get(this.apiUrl, this.authService.addAccessTokenToRequest())
      .map((res:Response) => res.json())
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  findById(id: string): Observable<User> {
    return this.http.get(this.apiUrl + '/' + id, this.authService.addAccessTokenToRequest())
    .map((res:Response) => res.json())
    .catch((error:any) => Observable.throw(error.json().error || 'Error'));
  }

  saveUser(user: User): Observable<User> {
    return this.http.post(this.apiUrl, user, this.authService.addAccessTokenToRequest())
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));

  }

  deleteUserById(id: string): Observable<boolean> {
    return this.http.delete(this.apiUrl + '/' + id, this.authService.addAccessTokenToRequest())
    /*.map((res:Response) => res.json())*/
    .catch((error:any) => Observable.throw(error.json().error || 'Server error'));

  }

  updateUser(user: User): Observable<User> {
    return this.http.put(this.apiUrl + '/' + user.id, user, this.authService.addAccessTokenToRequest())
     .map((res:Response) => res.json())
     .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

}
