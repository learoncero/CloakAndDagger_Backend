package at.fhv.game.repository;

import at.fhv.game.model.Game;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class PublicGameRepository {
    private final HashMap<String, Game> games = new HashMap<>();

    public void save(Game game) {
        games.put(game.getGameCode(), game);
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

    public List<Game> findAll() {
        return List.copyOf(games.values());
    }
}
