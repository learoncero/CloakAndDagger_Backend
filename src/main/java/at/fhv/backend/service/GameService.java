package at.fhv.backend.service;

import at.fhv.backend.model.Game;
import at.fhv.backend.model.GameStatus;
import at.fhv.backend.model.Player;
import at.fhv.backend.model.Role;
import at.fhv.backend.repository.GameRepository;
import at.fhv.backend.utils.GameCodeGenerator;
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
        Game game = new Game(generateGameCode(), numberOfPlayers, numberOfImpostors, map);

        gameRepository.save(game);

        return game;
    }

    private String generateGameCode() {
        return GameCodeGenerator.generateGameCode();
    }

    public Game getGameByCode(String gameCode) {
        return gameRepository.findByGameCode(gameCode);
    }

    public Game startGame(String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null) {
            gameRepository.save(game);
        }
        return game;
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
//                System.out.println("Player to kill: " + player.getId());
                if (player.getRole().equals(Role.CREWMATE)) {
                    player.setRole(Role.CREWMATE_GHOST);
                    gameRepository.save(game);
                } else if (player.getRole().equals(Role.IMPOSTOR)) {
                    player.setRole(Role.IMPOSTOR_GHOST);
                    gameRepository.save(game);
                }
                List<Player> crewmates = game.getPlayers().stream().filter(p -> p.getRole().equals(Role.CREWMATE)).toList();
                if (crewmates.size() == game.getNumberOfImpostors()) {
                    game.setGameStatus(GameStatus.IMPOSTORS_WIN);
                    gameRepository.save(game);
                }
                return game;
            }
        }
        return null;
    }
}
