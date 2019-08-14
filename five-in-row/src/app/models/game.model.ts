export interface Game {
    id: string;
    players: Array<string>;
    status: string;
    winner: Array<string>;
    grid: Array<Array<string>>;
  }