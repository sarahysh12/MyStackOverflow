import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';
import { UserSignupComponent } from './user-signup/user-signup.component';
import { UserSigninComponent } from './user-signin/user-signin.component';
import { UserProfileComponent } from './user-profile/user-profile.component';


const routes: Routes = [
{path: 'users', component: UserListComponent},
{path: 'users/signup', component: UserSignupComponent},
{path: 'users/edit/:id', component: UserSignupComponent},
{path: 'users/login', component: UserSigninComponent},
{path: 'users/:id', component: UserProfileComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
