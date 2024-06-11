package at.fhv.game.service;

import at.fhv.game.model.*;
import at.fhv.game.model.messages.PlayerMoveMessage;
import at.fhv.game.repository.PrivateGameRepository;
import at.fhv.game.repository.PublicGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final PrivateGameRepository privateGameRepository;
    private final PublicGameRepository publicGameRepository;
    private final TaskService taskService;
    private ConcurrentHashMap<Integer, PlayerActivity> playerActivities = new ConcurrentHashMap<>();

    @Autowired
    public GameService(PrivateGameRepository privateGameRepository, PublicGameRepository publicGameRepository, TaskService taskService) {
        this.privateGameRepository = privateGameRepository;
        this.publicGameRepository = publicGameRepository;
        this.taskService = taskService;
    }

    public Game createGame(GameMode gameMode, int numberOfPlayers, int numberOfImpostors, String map) {
        Game game = new Game(gameMode, numberOfPlayers, numberOfImpostors, map);

        if (gameMode.equals(GameMode.PRIVATE)) {
            privateGameRepository.save(game);
        } else {
            publicGameRepository.save(game);
        }

        return game;
    }

    public Game getGameByCode(String gameCode) {
        Game game = privateGameRepository.findByGameCode(gameCode);
        if (game == null) {
            game = publicGameRepository.findByGameCode(gameCode);
        }
        return game;
    }

    public boolean startGame(String gameCode) {
        Game game = getGameByCode(gameCode);
        if (game != null) {
            game.setGameStatus(GameStatus.IN_GAME);
            if (game.getGameMode().equals(GameMode.PRIVATE)) {
                privateGameRepository.save(game);
            } else {
                publicGameRepository.save(game);
            }
            return true;
        }
        return false;
    }

    public Game killPlayer(String gameCode, int playerId, int taskId) {
        Game game = getGameByCode(gameCode);
        if (game != null) {
            if (taskId != -1 && taskService.getStatus(game, taskId)) {
                taskService.setStatus(game, taskId, false);
            }

            Player player = game.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);
            if (player != null) {
                if (player.getRole().equals(Role.CREWMATE)) {
                    player.setRole(Role.CREWMATE_GHOST);
                } else if (player.getRole().equals(Role.IMPOSTOR)) {
                    player.setRole(Role.IMPOSTOR_GHOST);
                }
                player.setDeadBodyPosition(player.getPlayerPosition());
                checkImpostorWin(game);
                if (game.getGameMode().equals(GameMode.PRIVATE)) {
                    privateGameRepository.save(game);
                } else {
                    publicGameRepository.save(game);
                }
            }
        }
        return game;
    }

    public void checkImpostorWin(Game game) {
        if (game.getPlayers().stream().filter(p -> p.getRole().equals(Role.CREWMATE)).count() == game.getPlayers().stream().filter(p -> p.getRole().equals(Role.IMPOSTOR)).count()) {
            game.setGameStatus(GameStatus.IMPOSTORS_WIN);
        }
    }

    public void checkCrewmatesWin(Game game) {
        if ((game.getNumberOfImpostors() > 0) && (game.getPlayers().stream().filter(p -> p.getRole().equals(Role.IMPOSTOR)).count() == 0)) {
            game.setGameStatus(GameStatus.CREWMATES_WIN);
        }
        if (game.getTasks().stream().filter(t -> !t.isCompleted()).count() == 0) {
            game.setGameStatus(GameStatus.CREWMATES_WIN);
        }
    }

    public void updatePlayerActivity(int playerId, String gameCode) {
        long currentTime = System.currentTimeMillis();
        PlayerActivity activity = new PlayerActivity(currentTime, gameCode);
        playerActivities.put(playerId, activity);
    }

    public List<PlayerMoveMessage> checkInactivity() {

        long now = System.currentTimeMillis();
        List<PlayerMoveMessage> inactiveMessages = new ArrayList<>();

        playerActivities.forEach((playerId, activity) -> {

            Game game = getGameByCode(activity.getGameCode());
            if (game != null) {

                game.getPlayers().stream()
                        .filter(p -> p.getId() == playerId && p.isMoving())
                        .findFirst()
                        .ifPresent(player -> {

                            if ((now - activity.getLastMoveTime()) > 251) {
                                player.setMoving(false);
                                PlayerMoveMessage message = new PlayerMoveMessage(
                                        playerId,
                                        null,
                                        game.getGameCode(),
                                        player.isMirrored(),
                                        false
                                );
                                inactiveMessages.add(message);

                            }
                        });
            }
        });


        return inactiveMessages;
    }

    public Game reportBody(String gameCode, int bodyToReportId) {
        Game game = getGameByCode(gameCode);
        if (game != null) {
            Player bodyToReport = game.getPlayers().stream().filter(p -> p.getId() == bodyToReportId).findFirst().orElse(null);
            if (bodyToReport != null) {
                bodyToReport.setDeadBodyPosition(new Position(-1, -1));
                game.getReportedBodies().add(bodyToReport.getId());
            }
        }
        return game;
    }

    public Game setRandomSabotagePosition(String gameCode, int sabotageId, Position position) {
        Game game = getGameByCode(gameCode);
        if (game != null) {
            Optional<Sabotage> sabotage = game.getSabotages().stream()
                    .filter(s -> s.getId() == sabotageId)
                    .findFirst();
            if (sabotage.isPresent()) {
                sabotage.get().setPosition(position);
            }
        }

        return game;
    }

    public Game setRandomWallPositionsForSabotage(String gameCode, int sabotageId, List<Position[]> wallPositions) throws Exception {
        Game game = getGameByCode(gameCode);
        if (game != null) {
            Optional<Sabotage> sabotage = game.getSabotages().stream()
                    .filter(s -> s.getId() == sabotageId)
                    .findFirst();
            if (sabotage.isPresent() && sabotageId == 4) {
                sabotage.get().setWallPositions(wallPositions);
            }
        }

        return game;
    }


    public Game endGame(String gameCode) {
        Game game = getGameByCode(gameCode);
        game.setGameStatus(GameStatus.IMPOSTORS_WIN);

        if (game.getGameMode().equals(GameMode.PRIVATE)) {
            privateGameRepository.save(game);
        } else {
            publicGameRepository.save(game);
        }
        return game;
    }

    public Game cancelSabotage(Game game) {
        for (Sabotage s : game.getSabotages()) {
            if (s.getPosition() != null) {
                s.setPosition(new Position(-1, -1));
            }
            if (s.getId() == 4 && s.getWallPositions() != null) {
                List<Position[]> updatedWallPositions = new ArrayList<>();
                for (Position[] wallPosition : s.getWallPositions()) {
                    updatedWallPositions.add(new Position[]{new Position(-1, -1), new Position(-1, -1)});
                }
                s.setWallPositions(updatedWallPositions);
            }
        }
        if (game.getGameMode().equals(GameMode.PRIVATE)) {
            privateGameRepository.save(game);
        } else {
            publicGameRepository.save(game);
        }
        return game;
    }


    public Game eliminatePlayer(String gameCode, int playerId) {
        Game game = getGameByCode(gameCode);
        if (game != null) {
            Player player = game.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);
            if (player != null) {
                switch (player.getRole()) {
                    case CREWMATE:
                        player.setRole(Role.CREWMATE_GHOST);
                        break;
                    case IMPOSTOR:
                        player.setRole(Role.IMPOSTOR_GHOST);
                        break;
                    default:
                        break;
                }
                player.setDeadBodyPosition(player.getPlayerPosition());
                if (game.getGameMode().equals(GameMode.PRIVATE)) {
                    privateGameRepository.save(game);
                } else {
                    publicGameRepository.save(game);
                }
            }
        }
        return game;
    }

    public String checkDuelResult(String choice) {
        String[] choices = {"Rock", "Paper", "Scissors"};
        Random random = new Random();
        String opponentChoice = choices[random.nextInt(choices.length)];
        String result;

        if (choice.equals(opponentChoice)) {
            result = "Draw";
        } else if ((choice.equals("Rock") && opponentChoice.equals("Scissors")) ||
                (choice.equals("Paper") && opponentChoice.equals("Rock")) ||
                (choice.equals("Scissors") && opponentChoice.equals("Paper"))) {
            result = "Win";
        } else {
            result = "Lose";
        }
        return result;
    }

    public Game updateWallPositionsByResult(String gameCode, String result) {
        Game game = getGameByCode(gameCode);
        if (game != null && result.equals("Win")) {
            for (Sabotage s : game.getSabotages()) {
                if (s.getId() == 4 && s.getWallPositions() != null) {
                    List<Position[]> updatedWallPositions = new ArrayList<>();
                    for (Position[] wallPosition : s.getWallPositions()) {
                        updatedWallPositions.add(new Position[]{new Position(-1, -1), new Position(-1, -1)});
                    }
                    s.setWallPositions(updatedWallPositions);
                }
            }
            if (game.getGameMode().equals(GameMode.PRIVATE)) {
                privateGameRepository.save(game);
            } else {
                publicGameRepository.save(game);
            }
        }
        return game;
    }

    public List<Game> getPublicGames() {
        return publicGameRepository.findAll();
    }
}