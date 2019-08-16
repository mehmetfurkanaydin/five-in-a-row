import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

import { GameService } from '../services/game.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.less']
})
export class WelcomeComponent implements OnInit {
  username: string;
  gameSub: Subscription;
  message: String;

  constructor(private router: Router, private gameService: GameService) {
    this.gameSub = this.gameService.getGameStartListener()
    .subscribe((data) => {
      if (data === 'waiting') {
        this.message = 'Waiting for second player!';
        this.gameService.waitTurn();
      } else if (data === 'full') {
        this.message = 'Game is full!';
      } else {
        this.router.navigate(["game"]);
      }
    });
   }

  ngOnInit() {
  }

  login() : void {
    this.gameService.startGame(this.username);
  }

}
