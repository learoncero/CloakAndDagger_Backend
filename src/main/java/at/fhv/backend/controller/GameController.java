package at.fhv.backend.controller;

import at.fhv.backend.model.Game;
import at.fhv.backend.model.Map;
import at.fhv.backend.model.Player;
import at.fhv.backend.model.Position;
import at.fhv.backend.model.messages.CreateGameMessage;
import at.fhv.backend.model.messages.PlayerJoinMessage;
import at.fhv.backend.model.messages.PlayerKillMessage;
import at.fhv.backend.model.messages.PlayerMoveMessage;
import at.fhv.backend.service.GameService;
import at.fhv.backend.service.MapService;
import at.fhv.backend.service.PlayerService;
import at.fhv.backend.service.SabotageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class GameController {
    private final GameService gameService;
    private final PlayerService playerService;
    private final SabotageService sabotageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MapService mapService;

    @Autowired
    public GameController(GameService gameService, PlayerService playerservice, SabotageService sabotageService, SimpMessagingTemplate messagingTemplate, MapService mapService) {
        this.gameService = gameService;
        this.playerService = playerservice;
        this.sabotageService = sabotageService;
        this.messagingTemplate = messagingTemplate;
        this.mapService = mapService;
    }


    @PostMapping("/game")
    public ResponseEntity<Game> createGame(@RequestBody CreateGameMessage createGameMessage) throws Exception {
        Game game = gameService.createGame(createGameMessage.getNumberOfPlayers(), createGameMessage.getNumberOfImpostors(), createGameMessage.getMap());

        Position randomPosition = mapService.getRandomWalkablePosition(game.getMap());
        Player player = playerService.createPlayer(createGameMessage.getPlayer().getUsername(), randomPosition, game);
        // Assign roles to players (get Impostor Player Indices)
        player = playerService.setInitialRandomRole(game.getNumberOfPlayers(), game.getNumberOfImpostors(), player);
        game.getPlayers().add(player);

        // Check if sabotages have already been added
        if (game.getSabotages() == null || game.getSabotages().isEmpty()) {
            sabotageService.addSabotagesToGame(game);
        }

        return ResponseEntity.ok(game);
    }

    @GetMapping("/game/{gameCode}")
    public ResponseEntity<Game> getGameByCode(@PathVariable String gameCode) {
        Game game = gameService.getGameByCode(gameCode);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/game/join")
    public ResponseEntity<?> createPlayer(@RequestBody PlayerJoinMessage joinMessage) {
        if (joinMessage == null || joinMessage.getPosition() == null || joinMessage.getGameCode() == null) {
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

            if (game.getPlayers().stream().anyMatch(p -> p.getUsername().equals(joinMessage.getUsername()))) {
                return ResponseEntity.badRequest().body("Username is already taken");
            }

            Position randomPosition = mapService.getRandomWalkablePosition(game.getMap());
            Player player = playerService.createPlayer(joinMessage.getUsername(), randomPosition, game);
            game.getPlayers().add(player);

            //Assign roles randomly to players
            game.setPlayers(playerService.setRandomRole(game.getPlayers()));
            return ResponseEntity.ok()
                    .body(game);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating player: " + e.getMessage());
        }
    }

    //Todo: handle case when game is null
    @MessageMapping("/{gameCode}/play")
    public void playGame(@DestinationVariable String gameCode) {
        if (gameService.startGame(gameCode)) {
            // Send the game information to the corresponding topic
            messagingTemplate.convertAndSend("/topic/" + gameCode + "/play", gameCode);
        }
    }

    @MessageMapping("/move")
    @SendTo("/topic/positionChange")
    public Game movePlayer(@Payload PlayerMoveMessage playerMoveMessage) {
        int playerId = playerMoveMessage.getId();
        Game game = gameService.getGameByCode(playerMoveMessage.getGameCode());
        Player player = game.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);

        if (player != null) {
            Position newPosition = playerMoveMessage.getPosition();
            Map map = mapService.getMapByName(game.getMap());
            playerService.updatePlayerPosition(player, newPosition, map);

            return game;
        }

        return null;
    }

    @MessageMapping("/game/kill")
    public ResponseEntity<Game> handleKill(@Payload PlayerKillMessage playerKillMessage) {
        int playerToKillId = Integer.parseInt(playerKillMessage.getPlayerToKillId());
        String gameCode = playerKillMessage.getGameCode();
        System.out.println("Kill Request received. GameCode: " + gameCode + " PlayerId to be killed: " + playerToKillId);
        Game game = gameService.killPlayer(gameCode, playerToKillId);
        if (game != null) {
            messagingTemplate.convertAndSend("/topic/" + gameCode + "/kill/" + playerToKillId, game);
        }
        return ResponseEntity.notFound().build();
    }
}