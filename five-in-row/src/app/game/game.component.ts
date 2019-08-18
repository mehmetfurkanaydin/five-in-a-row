import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

import { GameService } from '../services/game.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.less']
})
export class GameComponent implements OnInit, OnDestroy {
  gameSub: Subscription;
  message = '';
  icon = '';
  status = '';
  grid = [
    ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
    ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
    ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
    ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
    ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
    ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
  ];
  username: string;
  turn: string;
  winner: string;
  tie: boolean = false;

  constructor(private gameService: GameService, private router: Router, private activatedRoute : ActivatedRoute) { 
    this.username = this.gameService.getCurrentUsername();
    this.gameSub = this.gameService.getGameListener()
    .subscribe((data) => {
      this.updateGrid(data.grid);
      this.icon = data.players[this.username];
      this.status = data.status;
      this.turn = data.turn;
      this.winner = data.winner;
      this.tie = data.tie;
      this.message = this.turn + "'s turn!"
      if (this.winner) {
        alert("Winner is " + this.winner + "!");
        this.router.navigate(['/']);
      } else if (this.tie) {
        alert("Tie!");
        this.router.navigate(['/']);
      }
    });
  };

  updateGrid(gridData) {
    for(let i = 0; i < 6; i++) {
      for (let j = 0; j < 9; j++) {
        this.grid[i][j] = gridData[i][j];
      }
    }
  }

  disconnect() {
    this.gameService.disconnectUser();
    this.router.navigate(['/']);
  }

  makeMove(row, colm) {
    this.gameService.makeMove(row,colm);
  }

  ngOnInit() {
    const gameData = this.gameService.getGameData();
    this.updateGrid(gameData.grid);
    this.icon = gameData.players[this.username];
    this.status = gameData.status;
    this.turn = gameData.turn;
    this.message = this.turn + "'s turn!"
  }

  ngOnDestroy() {
    this.gameSub.unsubscribe();
  }

}
