import { Component, OnInit } from '@angular/core';
import { UserService } from "../user.service";
import {ActivatedRoute, Router} from '@angular/router';
import { User } from "../user";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  providers: [UserService]
})
export class UserProfileComponent implements OnInit {

  id: string;
  user: User;
  private sub: any;

 constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService){}

  ngOnInit() {
      this.sub = this.route.params.subscribe(params => {
      this.id = params['id'];
    });
    this.getUser();
  }

  getUser(){
    this.userService.findById(this.id).subscribe(
      user => {
        this.user = user;
        }
        ,error => {
        console.log(error);
      }
    );

  }



}
