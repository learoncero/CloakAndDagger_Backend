package at.fhv.backend.service;

import at.fhv.backend.model.Game;
import at.fhv.backend.model.GameStatus;
import at.fhv.backend.model.Player;
import at.fhv.backend.model.Role;
import at.fhv.backend.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(int numberOfPlayers, int numberOfImpostors, String map) {
        Game game = new Game(numberOfPlayers, numberOfImpostors, map);

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

    public Game setGameAttributes(String gameCode, List<Player> players) {
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null) {
            game.setPlayers(players);
            gameRepository.save(game);
        }
        return game;
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
                if (game.getPlayers().stream().filter(p -> p.getRole().equals(Role.CREWMATE)).count() == game.getPlayers().stream().filter(p -> p.getRole().equals(Role.IMPOSTOR)).count()) {
                    game.setGameStatus(GameStatus.IMPOSTORS_WIN);
                }
                gameRepository.save(game);
                return game;
            }
        }
        return null;
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
}