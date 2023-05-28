import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChangePasswordComponent } from './components/change-password.component';
import { CollectionComponent } from './components/collection.component';
import { ListComponent } from './components/list.component';
import { LoginComponent } from './components/login.component';
import { MapComponent } from './components/map.component';
import { RegisterComponent } from './components/register.component';
import { ForgotPasswordComponent } from './components/forgot-password.component';
import { ReviewComponent } from './components/review.component';
import { SearchComponent } from './components/search.component';
import { UserListComponent } from './components/user-list.component';

const routes: Routes = [
  {path: "", component: SearchComponent},
  {path: "list/:query", component:ListComponent},
  {path: "login", component:LoginComponent},
  {path: "map/:name", component:MapComponent},
  {path: "register", component:RegisterComponent},
  {path: "forgotpassword", component:ForgotPasswordComponent},
  {path: "changepassword", component:ChangePasswordComponent},
  {path: "collection/:accountId", component:CollectionComponent},
  {path: "review/:linkId", component:ReviewComponent},
  {path: "users/:query", component:UserListComponent},
  {path: "", redirectTo: "/", pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
