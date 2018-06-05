import { Component, OnInit } from '@angular/core';
import { User } from "../user";
import { UserService } from "../user.service";
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
  providers: [UserService]
})
export class UserListComponent implements OnInit {

  private users: User[];

  constructor(private router: Router,
              private userService: UserService,
              private cookieService: CookieService) { }

  ngOnInit() {
    var cookieValue = this.cookieService.get('access_token');
    this.getAllUsers();
  }

  getAllUsers() {
    this.userService.findAll().subscribe(
      users => {
        this.users = users;
      },
      err => {
        console.log(err);
      }

    );
  }

 getProfilePage(user: User) {
    if (user) {
      this.router.navigate(['/users', user.id]);
    }
  }

  redirectNewUserPage() {
    this.router.navigate(['/users/signup']);
  }

  editUserPage(user: User) {
    if (user) {
      this.router.navigate(['/users/edit', user.id]);
    }
  }

  deleteUser(user: User) {
    if(user){
      this.userService.deleteUserById(user.id).subscribe(
        res => {
          this.getAllUsers();
          this.router.navigate(['/users']);
          console.log('User deleted');
        }
      );
    }
  }

}
