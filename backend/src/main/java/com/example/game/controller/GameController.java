package com.example.game.controller;

import com.example.game.domain.Game;
import com.example.game.domain.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ForkJoinPool;

@CrossOrigin
@RestController
@RequestMapping("/api/game")
public class GameController {
    private Game activeGame;

    @GetMapping(value = "/newGame")
    public ResponseEntity<String> getGame(@RequestParam String username) {
        Game currentGame = getActiveGame();
        if (currentGame == null) {
            Game newGame = new Game(username);
            setActiveGame(newGame);
            return new ResponseEntity<>(newGame.getGameInfo().toString(), HttpStatus.OK);
        } else {
            if(currentGame.getCurrentPlayerCount() == 1) {
                currentGame.addPlayer(username);
                currentGame.setPlayerTurn(currentGame.getPlayers()[0].getUsername());
                return new ResponseEntity<>(currentGame.getGameInfo().toString(), HttpStatus.OK);
            } else if (currentGame.getCurrentPlayerCount() == 2) {
                return new ResponseEntity<>("Game is Full", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Server error", HttpStatus.BAD_REQUEST);
    };

    @PostMapping(value = "/move")
    public ResponseEntity<String> playerMove(@RequestParam String username, @RequestParam String row, @RequestParam String colm) {
        Game currentGame = getActiveGame();
        if (currentGame.getPlayerTurn() != username) {
            return new ResponseEntity<>("Not your turn!", HttpStatus.BAD_REQUEST);
        }

        boolean result;
        if(currentGame.getPlayers()[0].getUsername() == username) {
            result = currentGame.insertToGrid(row, colm, currentGame.getPlayers()[0].getIcon());
        } else {
            result = currentGame.insertToGrid(row, colm, currentGame.getPlayers()[1].getIcon());
        }

        if (result) {
            return new ResponseEntity<>(currentGame.getGameInfo().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Server Error!", HttpStatus.BAD_REQUEST);
        }
    };

    @GetMapping("/turn")
    public DeferredResult<ResponseEntity<?>> playerMove(@RequestParam String username) {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        ForkJoinPool.commonPool().submit(() -> {
            Game currentGame = getActiveGame();
            while(currentGame.getPlayerTurn() != username) {}
            output.setResult(ResponseEntity.ok("Your Turn!"));
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
