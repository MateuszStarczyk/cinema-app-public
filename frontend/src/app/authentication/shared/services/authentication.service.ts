import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { NgxPermissionsService } from 'ngx-permissions';
import { UserCredentials } from '../models/user-credentials';
import { UserContext } from 'src/app/shared/models/user-context';
import { NewUser } from '../models/new-user';
import { MessageHubService } from 'src/app/shared/services/message-hub/message-hub.service';
import { AlertService } from 'src/app/shared/services/alerts/alert.service';
import { environment } from '../../../../environments/environment';
import { MessageResponse } from '../../../shared/models/message-response';
import { TokenStorageService } from './token-storage.service';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  baseUrl = environment.baseUrl;

  constructor(
    private readonly http: HttpClient,
    private readonly messageHub: MessageHubService,
    private readonly permissions: NgxPermissionsService,
    private readonly alerts: AlertService,
    private readonly tokenStorage: TokenStorageService
  ) {}

  login(credentials: UserCredentials): Observable<UserContext> {
    return this.http
      .post<UserContext>(this.baseUrl + '/api/auth/signin', credentials)
      .pipe(
        map((user) => {
          this.saveUser(user);
          return user;
        }),
        catchError((err) => {
          this.showLoginErrorMessage(err);
          return throwError(err);
        })
      );
  }

  logout() {
    this.clearUser();
    this.showLogoutSuccessMessage();
  }

  sessionExpired() {
    this.clearUser();
    this.showSessionExpiredMessage();
  }

  signup(newuser: NewUser): Observable<MessageResponse> {
    return this.http
      .post<MessageResponse>(this.baseUrl + '/api/auth/signup', newuser)
      .pipe(
        map((response) => {
          this.showSignupSuccessMessage(response.message);
          return response;
        }),
        catchError((err) => {
          this.showSignupErrorMessage(err);
          return throwError(err);
        })
      );
  }

  public loadLoggedInUser(): void {
    const user = this.tokenStorage.getUser();
    if (user) {
      this.messageHub.notifyUserContext(user);
      this.permissions.loadPermissions(user?.roles);
    } else {
      this.messageHub.notifyUserContext(null);
      this.permissions.flushPermissions();
    }
  }

  public getLoggedInUser(): UserContext {
    return this.tokenStorage.getUser();
  }

  private saveUser(user: UserContext) {
    this.tokenStorage.saveToken(user.accessToken);
    this.tokenStorage.saveUser(user);
    this.messageHub.notifyUserContext(user);
    this.permissions.loadPermissions(user?.roles);
  }

  private clearUser() {
    this.messageHub.notifyUserContext(null);
    this.permissions.flushPermissions();
    this.tokenStorage.signOut();
  }

  private showLoginErrorMessage(err: any) {
    if (err.status === 401) {
      this.alerts.showCustomErrorMessage('Wrong credentials!');
    } else {
      this.alerts.showDefaultErrorMessage();
    }
  }

  private showSignupSuccessMessage(message: string) {
    this.alerts.showCustomSuccessMessage(message);
  }

  private showSignupErrorMessage(err: any) {
    if (err.status === 400) {
      this.alerts.showCustomErrorMessage(err.error.message);
    } else {
      this.alerts.showDefaultErrorMessage();
    }
  }

  private showLogoutSuccessMessage() {
    this.alerts.showCustomSuccessMessage('You have successfully logged out');
  }

  private showSessionExpiredMessage() {
    this.alerts.showCustomWarningMessage('Your session has expired');
  }
}
