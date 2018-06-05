import { Injectable } from '@angular/core';
import {Http, Response } from "@angular/http";
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import { Observable } from "rxjs/Observable";
import { Answer } from "./answer";
import {AuthService} from "../user/auth.service";
@Injectable()
export class AnswerService {

private apiUrl = 'http://localhost:8080';

constructor(private http: Http,
              private authService: AuthService) { }

  findById(id: string): Observable<Answer> {
    return this.http.get(this.apiUrl + '/' + id, this.authService.addAccessTokenToRequest())
    .map((res:Response) => res.json())
    .catch((error:any) => Observable.throw(error.json().error || 'Error'));
  }

  saveAnswer(qid: string, answer: Answer): Observable<Answer> {
  return this.http.post(this.apiUrl + '/questions/' + qid + '/answers', answer, this.authService.addAccessTokenToRequest())
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  deleteAnswerById(id: string): Observable<boolean> {
    return this.http.delete(this.apiUrl + '/answers/' + id, this.authService.addAccessTokenToRequest())
    /*.map((res:Response) => res.json())*/
    .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  updateAnswer(answer: Answer): Observable<Answer> {
    return this.http.put(this.apiUrl + '/answers/' + answer.id, answer, this.authService.addAccessTokenToRequest())
     .map((res:Response) => res.json())
     .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

}
