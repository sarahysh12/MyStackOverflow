import { Injectable } from '@angular/core';
import { Http, Response, RequestOptions,URLSearchParams, Headers} from "@angular/http";
import {ActivatedRoute, Router} from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from "rxjs/Observable";


@Injectable()
export class AuthService {

constructor(private http: Http,
            private route: ActivatedRoute,
            private router: Router,
            private cookieService: CookieService) { }


getAccessToken(user: string, pass: string, navigate_to: string) {
  let params = new URLSearchParams();
  params.append('username', user);
  params.append('password', pass);
  params.append('grant_type','password');
  params.append('client_id','client');
  let headers = new Headers({'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 'Authorization': 'Basic '+btoa("client:secret")});
  let options = new RequestOptions({ headers: headers });
  this.http.post('http://localhost:8080/oauth/token', params.toString(), options)
    .map(res => res.json())
    .subscribe(
      data => this.saveToken(data),
      err => alert('Invalid Credentials'),
      () => this.router.navigate([navigate_to]));
}

saveToken(token){
    this.cookieService.set("refresh_token", token.refresh_token);
    this.cookieService.set("start_time", String((new Date()).getTime()));
    this.cookieService.set("expires_in", token.expires_in);
    this.cookieService.set("access_token", token.access_token);
  }

addAccessTokenToRequest(){
  var endTime = new Date();
  var diff = +endTime.getTime() - +this.cookieService.get("start_time");

  if(diff > +(this.cookieService.get("expires_in"))*1000){
        this.updateAccessToken();
  }
  let headers = new Headers({'Authorization': 'Bearer '+ this.cookieService.get("access_token")});
  let options = new RequestOptions({ headers: headers });
  return options;
}


  updateAccessToken(){
    let params = new URLSearchParams();
    params.append('grant_type','refresh_token');
    params.append('client_id','client');
    params.append('client_secret','secret');
    params.append('refresh_token', this.cookieService.get('refresh_token'));
    let headers = new Headers({'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 'Authorization': 'Basic '+btoa("client:secret")});
    let options = new RequestOptions({ headers: headers });
    this.http.post('http://localhost:8080/oauth/token', params.toString(), options)
      .map(res => res.json())
      .subscribe(
        data => this.saveToken(data),
        err => this.router.navigate(["/users/login"]));
  }


}
