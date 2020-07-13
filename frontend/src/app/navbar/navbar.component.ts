import { Component, OnInit, OnDestroy } from '@angular/core';
import {
  trigger,
  state,
  style,
  transition,
  animate,
} from '@angular/animations';
import { MessageHubService } from 'src/app/shared/services/message-hub/message-hub.service';
import { Subscription } from 'rxjs';
import { UserContext } from '../shared/models/user-context';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  subscribtions: Subscription[] = [];
  user: UserContext;

  constructor(private readonly messageHub: MessageHubService) {}

  ngOnInit(): void {
    this.subscribtions.push(
      this.messageHub.currentUser.subscribe((user) => (this.user = user))
    );
  }

  ngOnDestroy(): void {
    this.subscribtions.forEach((s) => {
      s.unsubscribe();
    });
  }
}
