import { Component, OnInit } from '@angular/core';
import { NewUser } from '../shared/models/new-user';
import { AuthenticationService } from '../shared/services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss'],
})
export class SignUpComponent implements OnInit {
  newuser: NewUser = new NewUser();
  isLoading = false;

  constructor(
    private readonly authenticationService: AuthenticationService,
    private readonly router: Router
  ) {}

  ngOnInit() {}

  signUp() {
    this.isLoading = true;
    this.authenticationService.signup(this.newuser).subscribe(
      () => {
        this.router.navigate(['/']);
      },
      () => {
        this.isLoading = false;
      }
    );
  }
}
