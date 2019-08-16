import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

import { GameService } from '../services/game.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.less']
})
export class GameComponent implements OnInit {
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
  username = '';
  turn = '';

  constructor(private gameService: GameService) { 
    this.username = this.gameService.getCurrentUsername();
    this.gameSub = this.gameService.getGameListener()
    .subscribe((data) => {
      this.updateGrid(data.grid);
      this.icon = data.players[this.username];
      this.status = data.status;
      this.turn = data.turn;
    });
  };

  updateGrid(gridData) {
    for(let i = 0; i < 6; i++) {
      for (let j = 0; j < 9; j++) {
        this.grid[i][j] = gridData[i][j];
      }
    }
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
  }

}
