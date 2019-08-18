import { Component, OnInit, OnDestroy } from '@angular/core';
import {Router} from '@angular/router';

import { GameService } from '../services/game.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.less']
})
export class WelcomeComponent implements OnInit, OnDestroy {
  username: string;
  gameSub: Subscription;
  message: String;
  timeLeft: number;
  interval;

  constructor(private router: Router, private gameService: GameService) {
    this.gameSub = this.gameService.getGameStartListener()
    .subscribe((data) => {
      if (data === 'waiting') {
        this.message = 'Waiting for second player!';
        this.gameService.waitTurn();
        this.timeLeft = 60;
        this.startTimer();
      } else if (data === 'full') {
        this.message = 'Game is full!';
      } else {
        clearInterval(this.interval);
        this.router.navigate(["game"]);
      }
    });
   }

   startTimer() {
    this.interval = setInterval(() => {
      if(this.timeLeft > 0) {
        this.timeLeft--;
      } else if (this.timeLeft == 0){
        alert('No one joined!');
        clearInterval(this.interval);
        this.router.navigate(["/"]);
      }
    },1000)
  }

  ngOnInit() {
  }

  login() : void {
    this.gameService.startGame(this.username);
  }

  ngOnDestroy() {
    this.gameSub.unsubscribe();
  }


}
