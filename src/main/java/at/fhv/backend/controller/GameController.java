package at.fhv.backend.controller;

import at.fhv.backend.model.CreateGameMessage;
import at.fhv.backend.model.Game;
import at.fhv.backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<Game> createGame(@RequestBody CreateGameMessage createGameMessage) {
        System.out.println("Received request to create game with username: " + createGameMessage.getUsername() + " number of players: " + createGameMessage.getNumberOfPlayers() + " number of impostors: " + createGameMessage.getNumberOfImpostors() + " map: " + createGameMessage.getMap());

        Game createdGame = gameService.createGame(createGameMessage.getUsername(), Integer.parseInt(createGameMessage.getNumberOfPlayers()), Integer.parseInt(createGameMessage.getNumberOfImpostors()), createGameMessage.getMap());

        if (createdGame != null) {
            return ResponseEntity.ok(createdGame);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{gameCode}")
    public ResponseEntity<Game> getGameByCode(@PathVariable String gameCode) {
        System.out.println("Received request to get game with code: " + gameCode);
        Game game = gameService.getGameByCode(gameCode);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
