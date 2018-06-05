import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../auth.service";
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-user-signin',
  templateUrl: './user-signin.component.html',
  styleUrls: ['./user-signin.component.css'],
  providers: [AuthService]
})
export class UserSigninComponent implements OnInit {

  user: string;
  pass: string;

  loginForm: FormGroup;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService) { }


  ngOnInit() {
      this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });

  }

   onSubmit() {
    this.user = this.loginForm.controls['username'].value;
    this.pass = this.loginForm.controls['password'].value;
    this.authService.getAccessToken(this.user, this.pass, "/questions");
  }

}
