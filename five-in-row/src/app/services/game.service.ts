import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';
import { map } from 'rxjs/operators';
import { find } from 'lodash';

import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class GameService {
  private gameUpdated = new Subject<any>();
  public username;

  constructor(private http: HttpClient) {}

  startGame(username)  {
    this.username = username;
    this.http
    .get<any>('http://localhost:8080/api/game/newGame?username=' + username)
    .subscribe(responseData => {
      this.gameUpdated.next(responseData);
    });
  }

  getCurrentUsername() {
    return this.username;
  }

  getGameListener() {
    return this.gameUpdated.asObservable();
  }
}