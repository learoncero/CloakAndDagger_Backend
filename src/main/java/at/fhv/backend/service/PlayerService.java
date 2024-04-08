package at.fhv.backend.service;

import at.fhv.backend.model.Game;
import at.fhv.backend.repository.PlayerRepository;
import at.fhv.backend.model.Player;
import at.fhv.backend.model.Position;
import at.fhv.backend.utils.RandomRoleAssigner;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final MapService mapService;

    public PlayerService(PlayerRepository playerRepository, MapService mapservice) {
        this.playerRepository = playerRepository;
        this.mapService = mapservice;
    }

    public Player createPlayer(String username, Position position, Game game) {
        Player player = new Player(username, position, game);
        playerRepository.save(player);
        return player;
    }

    public Player getPlayerByID(int id) {
        return playerRepository.findById(id).orElse(null);
    }

    public void updatePlayerPosition(int id, Position position) {
        System.out.println("updatePlayerPosition called in PlayerService with id: " + id + " and position: " + position.getX() + ", " + position.getY());
        Player player = playerRepository.findById(id).orElse(null);
        int x = position.getX();
        int y = position.getY();
        boolean outOfBounds =
                (x < 0) ||
                (y < 0) ||
                (y >= mapService.getMap().length) || //zeilen
                (x >= mapService.getMap()[0].length); //spalten

        if (player != null && mapService != null){
            if (!outOfBounds && mapService.isCellWalkable(x, y)) { //if true update repo otherwise do nothing
                player.setPosition(position);
                playerRepository.save(player);
                 /*//For debugging purposes
                List<Player> players = playerRepository.findAll();
                System.out.println("Validation with following players:");
                for(Player p: players) {
                    System.out.println("Player ID: " + p.getId() + ", Username " + p.getUsername() + ", Position: " + p.getPosition().getX() + ", " + p.getPosition().getY());
                }*/
            }
        }
    }


    public Player setInitialRandomRole(int numPlayers, int numImpostors, Player player) {
        //Create List of Random Indices that will be assigned as Impostors
        List<Integer> impostorsIndices = RandomRoleAssigner.assignRandomRoles(numPlayers, numImpostors);
        System.out.println("Impostorsindices : " + impostorsIndices);
        //Assign Impostor if player index matches impostor indeces
        if (impostorsIndices.contains(0)) {
                player.setRole("Impostor");
            }
        return player;
    }

    public List<Player> setRandomRole(List<Player> players) {
        //Create List of Random Indices that will be assigned as Impostors
        List<Integer> impostorsIndices = RandomRoleAssigner.getImpostorsIndices();
        System.out.println("Impostorsindices : " + impostorsIndices);
        //Assign Impostor if player index matches impostor indeces
        for (int i = 0; i < players.size(); i++) {
            if (impostorsIndices.contains(i)) {
                players.get(i).setRole("Impostor");
            }
        }
        return players;
    }
}
