package at.fhv.game.service;

import at.fhv.game.model.*;
import at.fhv.game.model.messages.PlayerMoveMessage;
import at.fhv.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private ConcurrentHashMap<Integer, PlayerActivity> playerActivities = new ConcurrentHashMap<>();

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(int numberOfPlayers, int numberOfImpostors, String map) {
        Game game = new Game(numberOfPlayers, numberOfImpostors, map);
        /*System.out.println("new Game in Create Game (GameService), Sabotages: ");
        for(Sabotage s: game.getSabotages()){
            System.out.println("Sabotage: " + s.getId() + ", (" + s.getPosition().getX() + ", " + s.getPosition().getY()+")");
        }*/
        gameRepository.save(game);

        return game;
    }

    public Game getGameByCode(String gameCode) {
        return gameRepository.findByGameCode(gameCode);
    }

    public boolean startGame(String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null) {
            game.setGameStatus(GameStatus.IN_GAME);
            gameRepository.save(game);
            return true;
        }
        return false;
    }

    public Game killPlayer(String gameCode, int playerId) {
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null) {
            Player player = game.getPlayers().stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);
            if (player != null) {
                if (player.getRole().equals(Role.CREWMATE)) {
                    player.setRole(Role.CREWMATE_GHOST);

                } else if (player.getRole().equals(Role.IMPOSTOR)) {
                    player.setRole(Role.IMPOSTOR_GHOST);
                }
                checkImpostorWin(game);
                gameRepository.save(game);
            }
        }
        return game;
    }

    private void checkImpostorWin(Game game) {
        if (game.getPlayers().stream().filter(p -> p.getRole().equals(Role.CREWMATE)).count() == game.getPlayers().stream().filter(p -> p.getRole().equals(Role.IMPOSTOR)).count()) {
            game.setGameStatus(GameStatus.IMPOSTORS_WIN);
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
            Game game = gameRepository.findByGameCode(activity.getGameCode());
            if (game != null) {

                game.getPlayers().stream()
                        .filter(p -> p.getId() == playerId && p.isMoving())
                        .findFirst()
                        .ifPresent(player -> {

                            if ((now - activity.getLastMoveTime()) > 1000) {
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
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null) {
            Player bodyToReport = game.getPlayers().stream().filter(p -> p.getId() == bodyToReportId).findFirst().orElse(null);
            if (bodyToReport != null) {
                game.getReportedBodies().add(bodyToReport.getId());
            }
        }
        return game;
    }


    public Game setRandomSabotagePosition(String gameCode, int sabotageId, Position position) {
        Game game = gameRepository.findByGameCode(gameCode);
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

    public Game endGame(String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode);
        game.setGameStatus(GameStatus.IMPOSTORS_WIN);

        gameRepository.save(game);
        return game;
    }

    public Game cancelSabotage(Game game) {
        for (Sabotage s : game.getSabotages()) {
            if (s.getPosition() != null) {
                s.setPosition(null);
            }
        }
        gameRepository.save(game);
        return game;
    }
}