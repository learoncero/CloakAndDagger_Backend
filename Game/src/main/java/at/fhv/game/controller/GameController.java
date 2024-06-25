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

import java.util.ArrayList;
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
    public GameController(GameService gameService, PlayerService playerService, TaskService taskService, SabotageService sabotageService, SimpMessagingTemplate messagingTemplate, MapService mapService) {
        this.gameService = gameService;
        this.playerService = playerService;
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
        Game game = gameService.createGame(createGameMessage.getGameMode(), createGameMessage.getNumberOfPlayers(), createGameMessage.getNumberOfImpostors(), createGameMessage.getMap());

        Position randomPosition = mapService.getRandomWalkablePosition(game.getMap());
        Player player = playerService.createPlayer(createGameMessage.getUsername(), randomPosition, game, createGameMessage.getPlayerColor());
        player = playerService.setInitialRandomRole(game.getNumberOfPlayers(), game.getNumberOfImpostors(), player);
        game.getPlayers().add(player);

        List<Position> taskPositions = mapService.getTaskPositions(game.getMap());
        ResponseEntity<List<MiniGame>> responseEntity = restTemplate.exchange(
                "http://localhost:5022/api/minigames",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        List<MiniGame> miniGames = responseEntity.getBody();
        if (miniGames != null && taskPositions != null) {
            taskService.addMiniGamesToGame(game, miniGames, taskPositions);
        }

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
        if (joinMessage == null || joinMessage.getGameCode() == null || joinMessage.getUsername() == null || joinMessage.getPlayerColor() == null || joinMessage.getGameMode() == null) {
            return ResponseEntity.badRequest().body(new CustomApiResponse<>(400, "Invalid join message", null));
        }

        try {
            Game game;
            if (joinMessage.getGameMode() == GameMode.PRIVATE) {
                game = gameService.getGameByCode(joinMessage.getGameCode());

                if (game == null) {
                    return ResponseEntity.notFound().build();
                }

                if (game.getPlayers().size() >= game.getNumberOfPlayers()) {
                    return ResponseEntity.badRequest().body(new CustomApiResponse<>(400, "Game lobby is full", null));
                }
            } else {
                List<Game> publicGames = gameService.getPublicGames();

                List<Game> availableGames = publicGames.stream()
                        .filter(g -> g.getPlayers().size() < g.getNumberOfPlayers())
                        .toList();

                if (availableGames.isEmpty()) {
                    return ResponseEntity.badRequest().body(new CustomApiResponse<>(400, "No available public games found", null));
                }

                game = availableGames.get(0);
            }

            if (game.getPlayers().stream().anyMatch(p -> p.getUsername().equals(joinMessage.getUsername()))) {
                return ResponseEntity.badRequest().body(new CustomApiResponse<>(400, "Username is already taken", null));
            }

            Position randomPosition = mapService.getRandomWalkablePosition(game.getMap());
            Player player = playerService.createPlayer(joinMessage.getUsername(), randomPosition, game, joinMessage.getPlayerColor());
            game.getPlayers().add(player);

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


    @MessageMapping("/{gameCode}/play")
    public void playGame(@DestinationVariable String gameCode) {
        if (gameService.startGame(gameCode)) {
            messagingTemplate.convertAndSend("/topic/" + gameCode + "/play", gameCode);
        }
    }

    @MessageMapping("/{gameCode}/move")
    @SendTo("/topic/{gameCode}/positionChange")
    public ResponseEntity<Game> movePlayer(@DestinationVariable String gameCode, @Payload PlayerMoveMessage playerMoveMessage) {
        int playerId = playerMoveMessage.getId();
        Game game = gameService.getGameByCode(gameCode);
        Player player = game.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);
        if (player != null) {
            Position newPosition = playerService.calculateNewPosition(player.getPlayerPosition(), playerMoveMessage.getKeyCode(), game.getSabotages(), player);

            Map map = mapService.getMapByName(game.getMap());
            playerService.updatePlayerPosition(player, newPosition, map, game.getSabotages(), game.getPlayers());

            playerService.updatePlayerMirrored(player, playerMoveMessage.isMirrored());
            playerService.updatePlayerisMoving(player, playerMoveMessage.isMoving());
            gameService.updatePlayerActivity(player.getId(), game.getGameCode());
            return ResponseEntity.ok().body(game);
        }

        return ResponseEntity.notFound().build();
    }

    @MessageMapping("game/{gameCode}/useVent")
    @SendTo("/topic/{gameCode}/useVent")
    public ResponseEntity<Game> handleVentUse(@Payload VentUsageMessage ventUsageMessage) {
        Game game = gameService.getGameByCode(ventUsageMessage.getGameCode());
        Player player = game.getPlayers().stream().filter(p -> p.getId() == ventUsageMessage.getPlayerId()).findFirst().orElse(null);
        List<Pair<Position>> ventPositions = mapService.getVentPositions(game.getMap());
        Position updatedPosition = playerService.sendPlayerToVent(player, ventPositions);
        game.getPlayers().stream().filter(p -> {
            assert player != null;
            return p.getId() == player.getId();
        }).findFirst().ifPresent(p -> p.setPlayerPosition(updatedPosition));
        return ResponseEntity.ok().body(game);
    }

    @MessageMapping("/game/{gameCode}/kill")
    @SendTo("/topic/{gameCode}/playerKill")
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

    @MessageMapping("/game/{gameCode}/report")
    @SendTo("/topic/{gameCode}/bodyReport")
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

    @MessageMapping("/game/{gameCode}/startSabotage")
    public ResponseEntity<Game> startSabotage(@DestinationVariable String gameCode, @Payload SabotageMessage sabotageMessage) throws Exception {
        int sabotageId = Integer.parseInt(sabotageMessage.getSabotageId());
        String mapName = sabotageMessage.getMap();
        Position randomPosition = mapService.getRandomWalkablePosition(mapName);
        Game game = gameService.setRandomSabotagePosition(gameCode, sabotageId, randomPosition);
        if (game != null) {
            if (sabotageId == 4) {
                List<Position[]> wallPositions = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    Position[] randomWallPosition = mapService.getRandomWallPosition(mapName);
                    wallPositions.add(randomWallPosition);
                }
                game = gameService.setRandomWallPositionsForSabotage(gameCode, sabotageId, wallPositions);
            }

            messagingTemplate.convertAndSend("/topic/" + gameCode + "/sabotageStart", ResponseEntity.ok().body(game));
            return ResponseEntity.ok().body(game);
        }

        return ResponseEntity.notFound().build();
    }

    @MessageMapping("/game/{gameCode}/cancelSabotage")
    @SendTo("/topic/{gameCode}/sabotageCancel")
    public ResponseEntity<Game> cancelSabotage(@DestinationVariable String gameCode) {
        Game game = gameService.getGameByCode(gameCode);
        if (game != null) {
            game = gameService.cancelSabotage(game);
            return ResponseEntity.ok().body(game);
        }

        return ResponseEntity.notFound().build();
    }

    @MessageMapping("/game/{gameCode}/submitDuelChoice")
    @SendTo("/topic/{gameCode}/duelChoiceResult")
    public ResponseEntity<Game> submitWallChoice(@DestinationVariable String gameCode, @Payload DuelChoiceMessage DuelMessage) {
        String result = gameService.checkDuelResult(DuelMessage.getChoice());
        Game game = gameService.updateWallPositionsByResult(gameCode, result);
        return ResponseEntity.ok().body(game);
    }

    @Scheduled(fixedRate = 251)
    public void handleInactivity() {
        List<PlayerMoveMessage> inactiveMessages = gameService.checkInactivity();
        inactiveMessages.forEach(message -> {
            int playerId = message.getId();
            String gameCode = message.getGameCode();
            Game updatedGame = gameService.getGameByCode(gameCode);
            Player player = updatedGame.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);
            playerService.updatePlayerisMoving(player, message.isMoving());

            messagingTemplate.convertAndSend("/topic/" + gameCode + "/idleChange", ResponseEntity.ok().body(updatedGame));
        });
    }

    @Scheduled(fixedRate = 1000)
    public void handleGameInactivity() {
        List<Game> inactiveGames = gameService.checkGameInactivity();
        inactiveGames.forEach(game -> {
            gameService.deleteGame(game);
            messagingTemplate.convertAndSend("/topic/" + game.getGameCode() + "/gameDeleted", ResponseEntity.ok().body("Game has been deleted due to inactivity"));
        });
    }

    @MessageMapping("/game/{gameCode}/end")
    @SendTo("/topic/{gameCode}/gameEnd")
    public ResponseEntity<Game> endGame(@Payload EndGameMessage endGameMessage) {
        Game game = gameService.endGame(endGameMessage.getGameCode());
        return ResponseEntity.ok().body(game);
    }

    @MessageMapping("/game/{gameCode}/startEmergencyMeeting")
    public ResponseEntity<Game> startEmergencyMeeting(@DestinationVariable String gameCode) {
        Game game = gameService.getGameByCode(gameCode);

        if (game != null) {
            restTemplate.postForEntity("http://localhost:5011/api/chat/" + gameCode + "/start", null, Void.class);
            messagingTemplate.convertAndSend("/topic/" + gameCode + "/emergencyMeetingStart", ResponseEntity.ok().body(game));
        }

        return ResponseEntity.notFound().build();
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

    @Operation(summary = "Get the voting Results of the last vote incl. all vote events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Vote or Game not found")
    })
    @PostMapping("/game/vote/voteResults") //todo update this
    public ResponseEntity<Void> handleVoteResults(@RequestBody VoteResultsMessage voteResultsMessage) {

        Game game = gameService.getGameByCode(voteResultsMessage.getGameCode());

        int intResults = voteResultsMessage.getVoteResult();
        game.setVotingResult(voteResultsMessage.getVoteResult());
        if (intResults >= 1) {
            game = gameService.eliminatePlayer(voteResultsMessage.getGameCode(), intResults);
            gameService.checkCrewmatesWin(game);
            gameService.checkImpostorWin(game);
        }

        List<VoteEvent> votings = voteResultsMessage.getVoteEvents();
        if (votings != null) {
            game.setVoteEvents(votings);
        }

        messagingTemplate.convertAndSend("/topic/" + voteResultsMessage.getGameCode() + "/voteResults", game);

        return ResponseEntity.ok().build();
    }
}
