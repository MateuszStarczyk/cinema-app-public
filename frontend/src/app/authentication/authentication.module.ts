import { NgModule } from '@angular/core';
import { LoginComponent } from './login/login.component';
import { SharedModule } from '../shared/shared.module';
import { SignUpComponent } from './sign-up/sign-up.component';
import { LogoutComponent } from './logout/logout.component';

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    LoginComponent,
    SignUpComponent,
    LogoutComponent
  ]
})
export class AuthenticationModule { }
