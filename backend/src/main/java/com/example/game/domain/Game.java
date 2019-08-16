package com.example.game.domain;


import com.google.gson.JsonObject;

public class Game {

    int row = 6;
    int colm = 9;
    private String[][] grid = new String[row][colm];
    private int maxPlayer = 2;
    private String status;
    private Player[] players = new Player[maxPlayer];
    private String playerTurn;
    private int moves = 0;
    private String winner;
    private boolean tie;

    public Game(String username) {
        initializeGrid();
        addPlayer(username);
    }

    public boolean isTie() {
        return tie;
    }

    public void setTie(boolean tie) {
        this.tie = tie;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColm() {
        return colm;
    }

    public void setColm(int colm) {
        this.colm = colm;
    }

    public void addPlayer(String username) {
       Player newUser = new Player(username);
       if(this.players[0] != null) {
           this.players[1] = newUser;
           this.players[1].setIcon("O");
           this.status = "ready";
       } else {
           this.players[0] = newUser;
           this.players[0].setIcon("X");
           this.status = "waiting-player";
       }
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setWinnerWithIcon(String icon) {
        if(getPlayers()[0].getIcon().equals(icon)) {
            setWinner(getPlayers()[0].getUsername());
        } else {
            setWinner(getPlayers()[1].getUsername());
        }
    }

    private void initializeGrid () {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < colm; j++) {
                this.grid[i][j] = "_";
            }
        }
    }

    private JsonObject getCurrentGrid () {
        JsonObject currentGrid = new JsonObject();
        for (int i = 0; i < row; i++) {
            JsonObject currentRow = new JsonObject();
            for (int j = 0; j < colm; j++) {
                currentRow.addProperty(Integer.toString(j), this.grid[i][j]);
            }
            currentGrid.add(Integer.toString(i), currentRow);
        }

        return currentGrid;
    };

    public String[][] getGrid () {
        return this.grid;
    }

    public JsonObject getGameInfo() {
        JsonObject currentGameInfo = new JsonObject();
        currentGameInfo.add("grid", getCurrentGrid());
        currentGameInfo.addProperty("status", this.status);
        currentGameInfo.addProperty("turn", getPlayerTurn());
        currentGameInfo.addProperty("winner", getWinner());
        currentGameInfo.addProperty("tie", isTie());

        JsonObject playerInfo = new JsonObject();

        if (this.players[0] != null) {
            playerInfo.addProperty(this.players[0].getUsername(), this.players[0].getIcon());
        }

        if (this.players[1] != null) {
            playerInfo.addProperty(this.players[1].getUsername(), this.players[1].getIcon());
        }

        currentGameInfo.add("players", playerInfo);
        return currentGameInfo;
    }

    public int getCurrentPlayerCount() {
        int player = 0;
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                player ++;
            }
        }
        return player;
    }

    public boolean insertToGrid(String row, String colm, String icon) {
        int x = Integer.parseInt(row);
        int y = Integer.parseInt(colm);

        if (this.grid[x][y] != "_") {
            return false;
        }

        this.grid[x][y] = icon;
        this.moves++;

        return true;
    }

    public int getMoves() {
        return this.moves;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
}
