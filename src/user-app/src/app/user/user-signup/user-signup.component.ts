import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../user.service";
import {User} from "../user";
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-user-signup',
  templateUrl: './user-signup.component.html',
  styleUrls: ['./user-signup.component.css'],
  providers: [UserService]
})
export class UserSignupComponent implements OnInit {

  id: string;
  user: User;

  userForm: FormGroup;
  private sub: any;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService) { }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.id = params['id'];
    });

    this.userForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [
        Validators.required,
        Validators.pattern("[^ @]*@[^ @]*")
      ]),
      password: new FormControl('', Validators.required)
    });


  if (this.id) { //edit form
    this.userService.findById(this.id).subscribe(
      user => {
          this.id = user.id;
          this.userForm.patchValue({
          username: user.username,
          email: user.email,
          password: user.password,
        });
      },error => {
        console.log(error);
      }
    );

  }

  }


  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  onSubmit() {
    if (this.userForm.valid) {
      if(this.id){
        let user: User = new User(this.id,
        this.userForm.controls['username'].value,
        this.userForm.controls['email'].value,
        this.userForm.controls['password'].value);
        this.userService.updateUser(user).subscribe();

     } else {
        let user: User = new User(null,
          this.userForm.controls['username'].value,
          this.userForm.controls['email'].value,
          this.userForm.controls['password'].value);
        this.userService.saveUser(user).subscribe();

      }

      this.userForm.reset();
      this.router.navigate(['/users']);
    }
  }

  redirectUserPage() {
    this.router.navigate(['/users']);

  }


}
