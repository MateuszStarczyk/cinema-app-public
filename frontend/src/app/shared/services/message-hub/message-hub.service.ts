import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { UserContext } from '../../models/user-context';

@Injectable({
  providedIn: 'root',
})
export class MessageHubService {
  private userContextSource = new Subject<UserContext>();

  public currentUser = this.userContextSource.asObservable();

  notifyUserContext(userContext: UserContext) {
    this.userContextSource.next(userContext);
  }
}
