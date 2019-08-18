import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from '../app-routing.module';

import {
  MatCardModule, MatInputModule, MatGridListModule
} from '@angular/material';

import { GameComponent } from './game.component';
import { WelcomeComponent } from '../welcome/welcome.component';
import { GameService } from '../services/game.service';

describe('GameComponent', () => {
  let component: GameComponent;
  let fixture: ComponentFixture<GameComponent>;
  let gameService: GameService;
  let gameData = {
    grid: [
      ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
      ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
      ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
      ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
      ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
      ['_', '_', '_', '_' , '_' , '_', '_' , '_', '_'],
    ],
    players : {
      player1: 'X',
      player2: 'O'
    },
    status: 'ready',
    turn: 'player1'
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        GameComponent,
        WelcomeComponent
      ],
      imports: [
        HttpClientModule,
        AppRoutingModule,
        FormsModule,
        MatGridListModule,
        MatCardModule,
        MatInputModule
      ]
    })
    .compileComponents();
    gameService = TestBed.get(GameService);
    gameService.username = "player1";
    spyOn(gameService, 'getGameData').and.returnValue(gameData);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
