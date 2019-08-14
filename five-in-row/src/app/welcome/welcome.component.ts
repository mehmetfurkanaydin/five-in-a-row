import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

import { GameService } from '../services/game.service';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.less']
})
export class WelcomeComponent implements OnInit {
  username: string;

  constructor(private router: Router, private gameService: GameService) { }

  ngOnInit() {
  }

  login() : void {
    this.gameService.startGame(this.username);
    this.router.navigate(["game"]);
  }

}
