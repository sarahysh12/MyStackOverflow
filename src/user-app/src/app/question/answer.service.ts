import { Injectable } from '@angular/core';
import {Http, Response } from "@angular/http";
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import { Observable } from "rxjs/Observable";
import { Answer } from "./answer";

@Injectable()
export class AnswerService {

private apiUrl = 'http://localhost:8080';

constructor(private http: Http) { }


  saveAnswer(answer: Answer): Observable<Answer> {
  return this.http.post(this.apiUrl, answer)
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));

  }

  deleteAnswerById(id: string): Observable<boolean> {
    return this.http.delete(this.apiUrl + '/questions/' + id + '/answers')
    /*.map((res:Response) => res.json())*/
    .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  updateAnswer(answer: Answer): Observable<Answer> {
    return this.http.put(this.apiUrl + '/answers/' + answer.id, answer)
     .map((res:Response) => res.json())
     .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }


}
