import { Injectable } from '@angular/core';
import {
  Router,
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from '@angular/router';
import { AuthenticationService } from 'src/app/authentication/shared/services/authentication.service';

@Injectable({ providedIn: 'root' })
export class AdminAuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const currentUser = this.authenticationService.getLoggedInUser();
    if (currentUser && currentUser?.roles.includes('ROLE_ADMIN')) {
      return true;
    }

    this.router.navigate(['/home']);
    return false;
  }
}
