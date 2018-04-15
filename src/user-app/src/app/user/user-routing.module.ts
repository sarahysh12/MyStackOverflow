import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';
import { UserSignupComponent } from './user-signup/user-signup.component';

const routes: Routes = [
{path: 'users', component: UserListComponent},
{path: 'users/signup', component: UserSignupComponent},
{path: 'users/edit/:id', component: UserSignupComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
