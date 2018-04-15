import { Injectable } from '@angular/core';
import { User } from "./user";
import { Http, Response } from "@angular/http";
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import { Observable } from "rxjs/Observable";

@Injectable()
export class UserService {

private apiUrl = 'http://localhost:8080/users';

constructor(private http: Http) {
  }

  findAll(): Observable<User[]>  {
    return this.http.get(this.apiUrl)
      .map((res:Response) => res.json())
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  findById(id: string): Observable<User> {
    return this.http.get(this.apiUrl + '/' + id)
    .map((res:Response) => res.json())
    .catch((error:any) => Observable.throw(error.json().error || 'Error'));
  }

  saveUser(user: User): Observable<User> {
    return this.http.post(this.apiUrl, user)
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));

  }

  deleteUserById(id: string): Observable<boolean> {
    return this.http.delete(this.apiUrl + '/' + id)
    /*.map((res:Response) => res.json())*/
    .catch((error:any) => Observable.throw(error.json().error || 'Server error'));

  }

  updateUser(user: User): Observable<User> {
    return this.http.put(this.apiUrl + '/' + user.id, user)
     .map((res:Response) => res.json())
     .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

}
