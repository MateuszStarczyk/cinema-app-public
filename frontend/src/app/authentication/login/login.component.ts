import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../shared/services/authentication.service';
import { UserCredentials } from '../shared/models/user-credentials';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenStorageService } from '../shared/services/token-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  credentials: UserCredentials = new UserCredentials();
  isLoading = false;
  navigateUrl: string;

  constructor(
    private readonly authenticationService: AuthenticationService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly tokenStorage: TokenStorageService
  ) {}

  ngOnInit() {
    const paramKey = 'returnUrl';
    this.navigateUrl = this.route.snapshot.queryParams[paramKey] || '/';

    // if user is already logged in navigate to next page
    if (this.tokenStorage.getToken()) {
      this.router.navigate([this.navigateUrl]);
    }
  }

  login() {
    this.isLoading = true;
    this.authenticationService.login(this.credentials).subscribe(
      () => {
        this.router.navigate([this.navigateUrl]);
      },
      () => {
        this.isLoading = false;
      }
    );
  }
}
