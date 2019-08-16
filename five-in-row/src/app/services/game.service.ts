import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';
import { map } from 'rxjs/operators';
import { find } from 'lodash';

import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class GameService {
  private gameUpdated = new Subject<any>();
  private gameStarted = new Subject<any>();
  public username;
  public gameData;

  constructor(private http: HttpClient) {}

  startGame(username)  {
    this.username = username;
    this.http
    .get<any>('http://localhost:8080/api/game/newGame?username=' + username)
    .subscribe(responseData => {
      if (responseData.status === 'Waiting for second player!') {
        this.gameStarted.next('waiting');
      } else if (responseData.status === 'Game is Full'){
        this.gameStarted.next('full');
      } else {
        this.gameData = responseData;
        this.gameStarted.next('ready');
        this.waitTurn();
      }
    });
  }

  getGameData() {
    return this.gameData;
  }

  makeMove(row,colm)  {
    this.http
    .post<any>('http://localhost:8080/api/game/makeMove', JSON.stringify({
      username: this.username,
      row: row,
      colm: colm
    }))
    .subscribe(responseData => {
      this.gameUpdated.next(responseData);
      this.waitTurn();
    });
  }

  waitTurn()  {
    this.http
    .get<any>('http://localhost:8080/api/game/turn?username=' + this.username)
    .subscribe(responseData => {
      this.gameData = responseData;
      this.gameStarted.next('ready');
      this.gameUpdated.next(responseData);
    });
  }

  getCurrentUsername() {
    return this.username;
  }

  getGameListener() {
    return this.gameUpdated.asObservable();
  }

  getGameStartListener() {
    return this.gameStarted.asObservable();
  }
}