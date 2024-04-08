package at.fhv.backend.controller;

import at.fhv.backend.messageModels.CreateGameMessage;
import at.fhv.backend.messageModels.PlayerJoinMessage;
import at.fhv.backend.model.*;
import at.fhv.backend.service.GameService;
import at.fhv.backend.service.PlayerService;
import at.fhv.backend.utils.RandomRoleAssigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;
    private final PlayerService playerService;

    @Autowired
    public GameController(GameService gameService, PlayerService playerservice) {
        this.gameService = gameService;
        this.playerService = playerservice;
    }

    @PostMapping("/create")
    public ResponseEntity<Game> createGame(@RequestBody CreateGameMessage createGameMessage) {
        System.out.println("Received request to create game with username: " + createGameMessage.getPlayer().getUsername() + " number of players: " + createGameMessage.getNumberOfPlayers() + " number of impostors: " + createGameMessage.getNumberOfImpostors() + " map: " + createGameMessage.getMap());

        Game createdGame = gameService.createGame(createGameMessage.getPlayer(), Integer.parseInt(createGameMessage.getNumberOfPlayers()), Integer.parseInt(createGameMessage.getNumberOfImpostors()), createGameMessage.getMap());

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

    @MessageMapping("/joinGame")
    @SendTo("/topic/playerJoined")
    public ResponseEntity<?> createPlayer(@Payload PlayerJoinMessage joinMessage) {
        if (joinMessage == null ||
                joinMessage.getUsername() == null ||
                joinMessage.getPosition() == null ||
                joinMessage.getGameCode() == null) {
            // Return a response indicating bad request if any required field is missing
            return ResponseEntity.badRequest().body("Invalid join message");
        }

        try {
            Game game = gameService.getGameByCode(joinMessage.getGameCode());

            if (game == null) {
                return ResponseEntity.notFound().build();
            }

            if (game.getPlayers().size() >= game.getNumberOfPlayers()) {
                return ResponseEntity.badRequest().body("Game lobby is full");
            }

            Player player = playerService.createPlayer(joinMessage.getUsername(), joinMessage.getPosition(), game);
            game.getPlayers().add(player);

            //Assign roles randomly to players
            game.setPlayers(playerService.setRandomRole(game.getPlayers()));

            return ResponseEntity.ok(game);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating player: " + e.getMessage());
        }
    }


    //Todo: check if null and random roles
    @MessageMapping("/{gameCode}/play")
    @SendTo("/topic/{gameCode}/play")
    public Game playGame(@RequestBody Game gameToPlay) {
        Game game = gameService.startGame(gameToPlay.getGameCode());
        /*System.out.println("Received request to play game with: " + gameToPlay.getGameCode() +
                            ", Player1: "+gameToPlay.getPlayers().get(0).getUsername() +
                            ", Position: "+gameToPlay.getPlayers().get(0).getPosition().getX());
        System.out.println("Game that got returned: "+ game.getGameCode() +
                            ", Player1: "+game.getPlayers().get(0).getUsername() +
                            ", Position: "+game.getPlayers().get(0).getPosition().getX());*/
        System.out.println("Player id and their roles in GameController: ");
        for (int i = 0; i < game.getPlayers().size(); i++){
            System.out.println("Player id: " + game.getPlayers().get(i).getId() +
                               " Role: " + game.getPlayers().get(i).getRole());
        }

        return game;
    }

}
