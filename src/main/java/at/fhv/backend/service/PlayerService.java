package at.fhv.backend.service;

import at.fhv.backend.repository.PlayerRepository;
import at.fhv.backend.model.Player;
import at.fhv.backend.model.Position;
import org.springframework.stereotype.Service;

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
        Player player = playerRepository.findById(id).orElse(null);
        if (player != null) {
            player.setPosition(position);
            playerRepository.save(player);
        }
    }
}
