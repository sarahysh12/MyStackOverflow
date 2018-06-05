import { Injectable } from '@angular/core';
import { Question } from "./question";
import {Http, Response } from "@angular/http";
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import { Observable } from "rxjs/Observable";
import {AuthService} from "../user/auth.service";

@Injectable()
export class QuestionService {

  private apiUrl = 'http://localhost:8080/questions';

  constructor(private http: Http,
              private authService: AuthService) { }

  findAll(): Observable<Question[]>  {
    return this.http.get(this.apiUrl, this.authService.addAccessTokenToRequest())
      .map((res:Response) => res.json())
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  findById(id: string): Observable<Question> {
    return this.http.get(this.apiUrl + '/' + id, this.authService.addAccessTokenToRequest())
    .map((res:Response) => res.json())
    .catch((error:any) => Observable.throw(error.json().error || 'Error'));
  }

  saveQuestion(question: Question): Observable<Question> {
  return this.http.post(this.apiUrl, question, this.authService.addAccessTokenToRequest())
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));

  }

  deleteQuestionById(id: string): Observable<boolean> {
    return this.http.delete(this.apiUrl + '/' + id, this.authService.addAccessTokenToRequest())
    /*.map((res:Response) => res.json())*/
    .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  updateQuestion(question: Question): Observable<Question> {
    return this.http.put(this.apiUrl + '/' + question.id, question, this.authService.addAccessTokenToRequest())
     .map((res:Response) => res.json())
     .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

}
