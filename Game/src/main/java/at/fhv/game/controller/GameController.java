package at.fhv.game.controller;

import at.fhv.game.model.*;
import at.fhv.game.model.messages.*;
import at.fhv.game.service.GameService;
import at.fhv.game.service.MapService;
import at.fhv.game.service.PlayerService;
import at.fhv.game.service.SabotageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            System.out.println("Invalid join message");
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Invalid join message", null));
        }

        try {
            Game game = gameService.getGameByCode(joinMessage.getGameCode());

            if (game == null) {
                System.out.println("Game not found");
                return ResponseEntity.notFound().build();
            }

            if (game.getPlayers().size() >= game.getNumberOfPlayers()) {
                System.out.println("Game lobby is full");
                return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Game lobby is full", null));
            }

            if (game.getPlayers().stream().anyMatch(p -> p.getUsername().equals(joinMessage.getUsername()))) {
                System.out.println("Username is already taken");
                return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Username is already taken", null));
            }

            Position randomPosition = mapService.getRandomWalkablePosition(game.getMap());
            Player player = playerService.createPlayer(joinMessage.getUsername(), randomPosition, game);
            game.getPlayers().add(player);

            //Assign roles randomly to players
            game.setPlayers(playerService.setRandomRole(game.getPlayers()));
            return ResponseEntity.ok().body(game);
        } catch (Exception e) {
            System.out.println("Error creating player: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(500, "Error creating player: " + e.getMessage(), null));
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
    public ResponseEntity<Game> movePlayer(@Payload PlayerMoveMessage playerMoveMessage) {
        int playerId = playerMoveMessage.getId();
        Game game = gameService.getGameByCode(playerMoveMessage.getGameCode());
        Player player = game.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);

        if (player != null) {
            Position newPosition = playerService.calculateNewPosition(player.getPosition(), playerMoveMessage.getKeyCode());
            Map map = mapService.getMapByName(game.getMap());
            playerService.updatePlayerPosition(player, newPosition, map);

            playerService.updatePlayerMirrored(player, playerMoveMessage.isMirrored());
            playerService.updatePlayerisMoving(player, playerMoveMessage.isMoving());
            gameService.updatePlayerActivity(player.getId(), game.getGameCode());
            return ResponseEntity.ok().body(game);
        }

        return ResponseEntity.notFound().build();
    }

    @MessageMapping("/game/kill")
    @SendTo("/topic/playerKill")
    public ResponseEntity<Game> handleKill(@Payload PlayerKillMessage playerKillMessage) {
        int playerToKillId = Integer.parseInt(playerKillMessage.getPlayerToKillId());
        String gameCode = playerKillMessage.getGameCode();
        Game game = gameService.killPlayer(gameCode, playerToKillId);

        if (game != null) {
            return ResponseEntity.ok().body(game);
        }

        return ResponseEntity.notFound().build();
    }

    @MessageMapping("/game/report")
    @SendTo("/topic/bodyReport")
    public ResponseEntity<Game> handleReportBody(@Payload BodyReportMessage bodyReportMessage) {
        int bodyToReportId = Integer.parseInt(bodyReportMessage.getBodyToReportId());
        String gameCode = bodyReportMessage.getGameCode();
        Game game = gameService.reportBody(gameCode, bodyToReportId);

        if (game != null) {
            return ResponseEntity.ok().body(game);
        }

        return ResponseEntity.notFound().build();
    }

    @Scheduled(fixedRate = 1000)
    public void handleInactivity() {
        List<PlayerMoveMessage> inactiveMessages = gameService.checkInactivity();
        inactiveMessages.forEach(message -> {
            int playerId = message.getId();
            Game updatedGame = gameService.getGameByCode(message.getGameCode());
            Player player = updatedGame.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);
            if (updatedGame != null) {

                playerService.updatePlayerisMoving(player, message.isMoving());

                messagingTemplate.convertAndSend("/topic/IdleChange", ResponseEntity.ok().body(updatedGame));
            }
        });
    }
}