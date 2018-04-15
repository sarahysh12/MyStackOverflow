import { Injectable } from '@angular/core';
import { Question } from "./question";
import {Http, Response } from "@angular/http";
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import { Observable } from "rxjs/Observable";

@Injectable()
export class QuestionService {

  private apiUrl = 'http://localhost:8080/questions';

  constructor(private http: Http) { }

  findAll(): Observable<Question[]>  {
    return this.http.get(this.apiUrl)
      .map((res:Response) => res.json())
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  findById(id: string): Observable<Question> {
    return this.http.get(this.apiUrl + '/' + id)
    .map((res:Response) => res.json())
    .catch((error:any) => Observable.throw(error.json().error || 'Error'));
  }

  saveQuestion(question: Question): Observable<Question> {
  return this.http.post(this.apiUrl, question)
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));

  }

  deleteQuestionById(id: string): Observable<boolean> {
    return this.http.delete(this.apiUrl + '/' + id)
    /*.map((res:Response) => res.json())*/
    .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  updateQuestion(question: Question): Observable<Question> {
    return this.http.put(this.apiUrl + '/' + question.id, question)
     .map((res:Response) => res.json())
     .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }


}
