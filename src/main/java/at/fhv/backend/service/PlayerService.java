package at.fhv.backend.service;

import at.fhv.backend.repository.PlayerRepository;
import at.fhv.backend.model.Player;
import at.fhv.backend.model.Position;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayer(int id, String username, int x, int y) {
        Player player = new Player(id, username, x, y);
        playerRepository.save(player);
        return player;
    }

    public Player getPlayerByID(int id) {
        return playerRepository.findById(id).orElse(null);
    }

    public void updatePlayerPosition(int id, Position position) {
        System.out.println("updatePlayerPosition called in PlayerService");
        Player player = playerRepository.findById(id).orElse(null);
        if (player != null) {
            player.setPosition(position);
            playerRepository.save(player);
            List<Player> players = playerRepository.findAll();
            for(Player p: players) {
                System.out.println("Player ID: " + p.getId() + " Player Username " + p.getUsername() + "Position: " + p.getPosition().getX() + ", " + p.getPosition().getY());
            }
        }
    }
}
