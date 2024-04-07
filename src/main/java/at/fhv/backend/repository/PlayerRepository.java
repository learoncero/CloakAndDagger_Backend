package at.fhv.backend.repository;

import at.fhv.backend.model.Player;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class PlayerRepository {
    private final HashMap<Integer, Player> players = new HashMap<>();

    public void save(Player player) {
        players.put(player.getId(), player);
        for (Player p : players.values()) {
            System.out.println("Repo: Player ID: " + p.getId() + " Username " + p.getUsername() +
                                " Position: " + p.getPosition().getX() + ", " + p.getPosition().getY());
        }
    }

    public Optional<Player> findById(int id) {
        return Optional.ofNullable(players.get(id));
    }

    public List<Player> findAll() {
        return new ArrayList<>(players.values());
    }
}
