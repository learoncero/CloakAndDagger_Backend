package at.fhv.game.controller;

import at.fhv.game.model.*;
import at.fhv.game.model.messages.*;
import at.fhv.game.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
import java.util.Objects;

@Controller
@RequestMapping("/api")
public class GameController {
    private final GameService gameService;
    private final PlayerService playerService;
    private final TaskService taskService;
    private final SabotageService sabotageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MapService mapService;
    private final org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();

    @Autowired
    public GameController(GameService gameService, PlayerService playerservice, TaskService taskService, SabotageService sabotageService, SimpMessagingTemplate messagingTemplate, MapService mapService) {
        this.gameService = gameService;
        this.playerService = playerservice;
        this.taskService = taskService;
        this.sabotageService = sabotageService;
        this.messagingTemplate = messagingTemplate;
        this.mapService = mapService;
    }

    @Operation(summary = "Create a new game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game created successfully",
                    content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @Schema(implementation = Game.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping("/game")
    public ResponseEntity<Game> createGame(@RequestBody CreateGameMessage createGameMessage) throws Exception {
        //Create game
        Game game = gameService.createGame(createGameMessage.getNumberOfPlayers(), createGameMessage.getNumberOfImpostors(), createGameMessage.getMap());

        //Create player, assign random position and role
        Position randomPosition = mapService.getRandomWalkablePosition(game.getMap());
        Player player = playerService.createPlayer(createGameMessage.getPlayer().getUsername(), randomPosition, game, createGameMessage.getPlayerColor());
        System.out.println(createGameMessage.getPlayer().getPlayerColor());
        player = playerService.setInitialRandomRole(game.getNumberOfPlayers(), game.getNumberOfImpostors(), player);
        game.getPlayers().add(player);

        //Get tasks, add Position and assign to game
        List<Position> taskPositions = mapService.getTaskPositions(game.getMap());
        ResponseEntity<List<MiniGame>> responseEntity = restTemplate.exchange(
                "http://localhost:5022/api/minigames",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MiniGame>>() {
                });
        // Retrieve the list of MiniGame objects from the response entity
        List<MiniGame> miniGames = responseEntity.getBody();
        if (miniGames != null && taskPositions != null) {
            taskService.addMiniGamesToGame(game, miniGames, taskPositions);
        }

        // Check if sabotages have already been added
        if (game.getSabotages() != null) {
            sabotageService.addSabotagesToGame(game);
        }

        return ResponseEntity.ok(game);
    }

    @Operation(summary = "Get game by code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @GetMapping("/game/{gameCode}")
    public ResponseEntity<Game> getGameByCode(@PathVariable String gameCode) {
        Game game = gameService.getGameByCode(gameCode);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Join a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player joined successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid join request"),
            @ApiResponse(responseCode = "404", description = "Game not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/game/join")
    public ResponseEntity<?> createPlayer(@RequestBody PlayerJoinMessage joinMessage) {
        if (joinMessage == null || joinMessage.getPosition() == null || joinMessage.getGameCode() == null) {
            return ResponseEntity.badRequest().body(new CustomApiResponse<>(400, "Invalid join message", null));
        }

        try {
            Game game = gameService.getGameByCode(joinMessage.getGameCode());

            if (game == null) {
                return ResponseEntity.notFound().build();
            }

            if (game.getPlayers().size() >= game.getNumberOfPlayers()) {
                return ResponseEntity.badRequest().body(new CustomApiResponse<>(400, "Game lobby is full", null));
            }

            if (game.getPlayers().stream().anyMatch(p -> p.getUsername().equals(joinMessage.getUsername()))) {
                return ResponseEntity.badRequest().body(new CustomApiResponse<>(400, "Username is already taken", null));
            }

            Position randomPosition = mapService.getRandomWalkablePosition(game.getMap());
            Player player = playerService.createPlayer(joinMessage.getUsername(), randomPosition, game, joinMessage.getPlayerColor() );
            game.getPlayers().add(player);

            //Assign roles randomly to players
            game.setPlayers(playerService.setRandomRole(game.getPlayers()));
            return ResponseEntity.ok().body(game);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomApiResponse<>(500, "Error creating player: " + e.getMessage(), null));
        }
    }

    @Operation(summary = "Leave a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player left successfully"),
            @ApiResponse(responseCode = "404", description = "Game or player not found")
    })
    @PostMapping("/game/{gameCode}/leave")
    public ResponseEntity<Boolean> removePlayer(@RequestBody PlayerLeaveMessage leaveMessage, @PathVariable String gameCode) {
        Game game = gameService.getGameByCode(gameCode);

        if (game != null) {
            Player player = game.getPlayers().stream().filter(p -> Objects.equals(p.getUsername(), leaveMessage.getUsername())).findFirst().orElse(null);
            if (player != null) {
                game.getPlayers().remove(player);
                return ResponseEntity.ok(true);
            }
        }

        return ResponseEntity.notFound().build();
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
            Position newPosition = playerService.calculateNewPosition(player.getPlayerPosition(), playerMoveMessage.getKeyCode());
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
        int taskId = -1;
        if (playerKillMessage.getNearbyTask() != null) {
            taskId = Integer.parseInt(playerKillMessage.getNearbyTask());
        }
        int playerToKillId = Integer.parseInt(playerKillMessage.getPlayerToKillId());
        String gameCode = playerKillMessage.getGameCode();
        Game game = gameService.killPlayer(gameCode, playerToKillId, taskId);

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
            restTemplate.postForEntity("http://localhost:5011/api/chat/" + gameCode + "/start", null, Void.class);
            return ResponseEntity.ok().body(game);
        }

        return ResponseEntity.notFound().build();
    }

    @MessageMapping("/game/startSabotage")
    @SendTo("/topic/sabotageStart")
    public ResponseEntity<Game> startSabotage(@Payload SabotageMessage sabotageMessage) throws Exception {
        int sabotageId = Integer.parseInt(sabotageMessage.getSabotageId());
        String gameCode = sabotageMessage.getGameCode();
        String mapName = sabotageMessage.getMap();
        Position randomPosition = mapService.getRandomWalkablePosition(mapName);
        Game game = gameService.setRandomSabotagePosition(gameCode, sabotageId, randomPosition);
        if (game != null) {
            return ResponseEntity.ok().body(game);
        }

        return ResponseEntity.notFound().build();
    }

    @MessageMapping("/game/{gameCode}/cancelSabotage")
    @SendTo("/topic/sabotageCancel")
    public ResponseEntity<Game> cancelSabotage(@DestinationVariable String gameCode) {
        Game game = gameService.getGameByCode(gameCode);
        if (game != null) {
            game = gameService.cancelSabotage(game);
            return ResponseEntity.ok().body(game);
        }

        return ResponseEntity.notFound().build();
    }

    @Scheduled(fixedRate = 251)
    public void handleInactivity() {
        List<PlayerMoveMessage> inactiveMessages = gameService.checkInactivity();
        inactiveMessages.forEach(message -> {
            int playerId = message.getId();
            Game updatedGame = gameService.getGameByCode(message.getGameCode());
            Player player = updatedGame.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);
            playerService.updatePlayerisMoving(player, message.isMoving());

            messagingTemplate.convertAndSend("/topic/IdleChange", ResponseEntity.ok().body(updatedGame));
        });
    }

    @MessageMapping("/game/end")
    @SendTo("/topic/gameEnd")
    public ResponseEntity<Game> endGame(@Payload EndGameMessage endGameMessage) {
        Game game = gameService.endGame(endGameMessage.getGameCode());

        System.out.println("Game ended");
        return ResponseEntity.ok().body(game);
    }

    @Operation(summary = "Conditionally mark a task as done")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task marked as done successfully"),
            @ApiResponse(responseCode = "404", description = "Game or task not found")
    })
    @PostMapping("/game/task/{gameCode}/done")
    public ResponseEntity<Boolean> taskDone(@PathVariable String gameCode, @RequestBody int taskId) {
        Game game = gameService.getGameByCode(gameCode);
        if (game != null) {
            if (taskService.taskDone(game, taskId)) {
                gameService.checkCrewmatesWin(game);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.ok(true);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get the isActive status of a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Game or task not found")
    })
    @PostMapping("/game/task/{gameCode}/status")
    public ResponseEntity<Boolean> getActiveStatus(@PathVariable String gameCode, @RequestBody int taskId) {
        Game game = gameService.getGameByCode(gameCode);

        if (game != null) {
            return ResponseEntity.ok(taskService.getStatus(game, taskId));
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Set the isActive status of a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status set successfully"),
            @ApiResponse(responseCode = "404", description = "Game or task not found")
    })
    @PostMapping("/game/task/{gameCode}/active")
    public ResponseEntity<Boolean> setActiveStatus(@PathVariable String gameCode, @RequestBody int taskId) {
        Game game = gameService.getGameByCode(gameCode);

        if (game != null && !taskService.getStatus(game, taskId)) {
            taskService.setStatus(game, taskId, true);
            return ResponseEntity.ok(true);
        } else if (game != null) {
            taskService.setStatus(game, taskId, false);
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get the playerId that got the most votes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PlayerId to be eliminated retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "PlayerId or Game not found")
    })
    @PostMapping("/game/vote/{gameCode}/voteResults/{results}")
    public ResponseEntity<Void> handleVoteResults(@PathVariable String gameCode, @PathVariable String results) {
        Game game = gameService.getGameByCode(gameCode);
        System.out.println("Vote Result received in GameController");
        System.out.println("gameCode: " + gameCode + " results: " + results);
        int intResults = Integer.parseInt(results);
        if (intResults >= 1) {
            game = gameService.eliminatePlayer(gameCode, intResults);
            gameService.checkCrewmatesWin(game);
            gameService.checkImpostorWin(game);
        }
        List<Integer> voteResults = game.getVotingResults();
        if (voteResults != null) {
            voteResults.add(intResults);
            game.setVotingResults(voteResults);
        }

        messagingTemplate.convertAndSend("/topic/voteResults", game);

        return ResponseEntity.ok().build();
    }
}