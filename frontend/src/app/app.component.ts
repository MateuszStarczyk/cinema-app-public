import {
  Component,
  AfterViewInit,
  ChangeDetectorRef,
} from '@angular/core';
import { AuthenticationService } from './authentication/shared/services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements AfterViewInit {
  title = 'cinema-app-frontend';

  constructor(
    private readonly authenticationService: AuthenticationService,
    private readonly cd: ChangeDetectorRef
  ) {}

  ngAfterViewInit(): void {
    this.authenticationService.loadLoggedInUser();
    this.cd.detectChanges();
  }
}
