package com.example.game.controller;

import com.example.game.domain.Game;
import com.example.game.domain.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

@CrossOrigin
@RestController
@RequestMapping("/api/game")
public class GameController {
    private Game activeGame;
    CountDownLatch latch;

    @GetMapping(value = "/newGame")
    public ResponseEntity<String> getGame(@RequestParam String username) {
        Game currentGame = getActiveGame();
        JsonObject response = new JsonObject();
        if (currentGame == null) {
            Game newGame = new Game(username);
            setActiveGame(newGame);
            response.addProperty("status", "Waiting for second player!");
            latch = new CountDownLatch(1);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } else {
            if(currentGame.getCurrentPlayerCount() == 1) {
                currentGame.addPlayer(username);
                currentGame.setPlayerTurn(currentGame.getPlayers()[0].getUsername());
                latch.countDown();
                return new ResponseEntity<>(currentGame.getGameInfo().toString(), HttpStatus.OK);
            } else if (currentGame.getCurrentPlayerCount() == 2) {
                response.addProperty("status", "Game is Full");
                return new ResponseEntity<>(response.toString(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Server error", HttpStatus.OK);
    };

    @PostMapping(value = "/makeMove")
    public ResponseEntity<String> playerMove(@RequestBody String moveDataJSON) {
        Game currentGame = getActiveGame();
        JsonObject moveData = new Gson().fromJson(moveDataJSON, JsonObject.class);
        if (!currentGame.getPlayerTurn().equals(moveData.get("username").getAsString())) {
            return new ResponseEntity<>("Not your turn!", HttpStatus.BAD_REQUEST);
        }

        boolean result;
        String row = moveData.get("row").getAsString();
        String colm = moveData.get("colm").getAsString();
        int nextPlayer;
        if(currentGame.getPlayers()[0].getUsername().equals(moveData.get("username").getAsString())) {
            result = currentGame.insertToGrid(row, colm, currentGame.getPlayers()[0].getIcon());
            nextPlayer = 1;
        } else {
            result = currentGame.insertToGrid(row, colm, currentGame.getPlayers()[1].getIcon());
            nextPlayer = 0;
        }

        if (result) {
            currentGame.setPlayerTurn(currentGame.getPlayers()[nextPlayer].getUsername());
            latch.countDown();
            return new ResponseEntity<>(currentGame.getGameInfo().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Server Error!", HttpStatus.BAD_REQUEST);
        }
    };

    @GetMapping("/turn")
    public DeferredResult<ResponseEntity<?>> playerTurn(@RequestParam String username) {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<ResponseEntity<?>>(800000l );

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(() -> {
            Game currentGame = getActiveGame();
            latch = new CountDownLatch(1);

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ResponseEntity response = new ResponseEntity<>(currentGame.getGameInfo().toString(), HttpStatus.OK);
            output.setResult(response);
        });

        return output;
    }

    @GetMapping("/deleteUsers")
    public ResponseEntity<String> deleteUsers() {

            Game currentGame = getActiveGame();
            currentGame.getPlayers()[0] = null;
            currentGame.getPlayers()[1] = null;

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    public Game getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(Game activeGame) {
        this.activeGame = activeGame;
    }
}
