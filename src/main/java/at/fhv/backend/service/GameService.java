package at.fhv.backend.service;

import at.fhv.backend.model.Game;
import at.fhv.backend.model.GameCodeGenerator;
import at.fhv.backend.model.Player;
import at.fhv.backend.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
    }

    public Game createGame(Player player, int numberOfPlayers, int numberOfImpostors, String map) {
        Game game = new Game(generateGameCode(), numberOfPlayers, numberOfImpostors, map);
        System.out.println("Game Code: " + game.getGameCode() + " Number of Players: " + game.getNumberOfPlayers());
        Player p = playerService.createPlayer(player.getId(), player.getUsername(), player.getPosition(), game);
        game.getPlayers().add(p);
        gameRepository.save(game);

        return game;
    }

    private String generateGameCode() {
        return GameCodeGenerator.generateGameCode();
    }

    public Game getGameByCode(String gameCode) {
        return gameRepository.findByGameCode(gameCode);
    }
}
