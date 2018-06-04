import { Injectable } from '@angular/core';
import {Http, Response } from "@angular/http";
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import { Observable } from "rxjs/Observable";
import { Comment } from "./comment";
import {AuthService} from "../user/auth.service";

@Injectable()
export class CommentService {

private apiUrl = 'http://localhost:8080';

constructor(private http: Http,
            private authService: AuthService) { }


  saveComment(aid: string, comment: Comment): Observable<Comment> {
  return this.http.post(this.apiUrl + '/answers/' + aid + '/comments', comment, this.authService.addAccessTokenToRequest())
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));

  }

  deleteCommentById(id: string): Observable<boolean> {
    return this.http.delete(this.apiUrl + '/comments/' + id, this.authService.addAccessTokenToRequest())
    /*.map((res:Response) => res.json())*/
    .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }


  updateComment(aid: string, cid: string, comment: Comment): Observable<Comment> {
    return this.http.put(this.apiUrl + '/answers/' + aid +'/comments/' + cid , comment, this.authService.addAccessTokenToRequest())
     .map((res:Response) => res.json())
     .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }


}
