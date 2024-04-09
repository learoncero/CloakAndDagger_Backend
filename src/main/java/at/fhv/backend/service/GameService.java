package at.fhv.backend.service;

import at.fhv.backend.model.Game;
import at.fhv.backend.utils.GameCodeGenerator;
import at.fhv.backend.model.Player;
import at.fhv.backend.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final SabotageService sabotageService;
    private final MapService mapService;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerService playerService, MapService mapService, SabotageService sabotageService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.mapService = mapService;
        this.sabotageService = sabotageService;
    }

    public Game createGame(Player player, int numberOfPlayers, int numberOfImpostors, String map) {
        Game game = new Game(generateGameCode(), numberOfPlayers, numberOfImpostors, map, mapService);

        // Check if sabotages have already been added
        if (game.getSabotages() == null || game.getSabotages().isEmpty()) {
            addSabotages(game);
        }

        System.out.println("Game Code: " + game.getGameCode() + " Number of Players: " + game.getNumberOfPlayers());
        Player p = playerService.createPlayer(player.getUsername(), player.getPosition(), game);

        // Assign roles to players (get Impostor Player Indices)
        p = playerService.setInitialRandomRole(game.getNumberOfPlayers(), game.getNumberOfImpostors(), p);
        game.getPlayers().add(p);
        gameRepository.save(game);

        System.out.println("Player id and their roles in GameServices create Game (Host): ");
        for (int i = 0; i < game.getPlayers().size(); i++) {
            System.out.println("Player id: " + game.getPlayers().get(i).getId() +
                    " Role: " + game.getPlayers().get(i).getRole());
        }

        return game;
    }

    private void addSabotages(Game game) {
        game.setSabotages(sabotageService.getAllSabotages());
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
}
