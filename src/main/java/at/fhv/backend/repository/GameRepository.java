package at.fhv.backend.repository;

import at.fhv.backend.model.Game;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Component
public class GameRepository {
    private final HashMap<String, Game> games = new HashMap<>();

    public void save(Game game) {
        games.put(game.getGameCode(), game);
        for (Game g : games.values()) {
//            System.out.println("Repo: Game Code: " + g.getGameCode() + " Number of Players: " + g.getNumberOfPlayers());
        }
    }

    public Game findByGameCode(String gameCode) {
        return games.get(gameCode);
    }

    public void deleteByGameCode(String gameCode) {
        games.remove(gameCode);
    }

    public boolean existsByGameCode(String gameCode) {
        return games.containsKey(gameCode);
    }

}
