import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from '../app-routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {
  MatCardModule, MatInputModule, MatGridListModule
} from '@angular/material';

import { WelcomeComponent } from './welcome.component';
import { GameComponent } from '../game/game.component';

describe('WelcomeComponent', () => {
  let component: WelcomeComponent;
  let fixture: ComponentFixture<WelcomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        WelcomeComponent,
        GameComponent
       ],
       imports: [
        AppRoutingModule,
        HttpClientModule,
        MatCardModule,
        MatInputModule,
        MatGridListModule,
        FormsModule,
        BrowserAnimationsModule
       ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WelcomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
