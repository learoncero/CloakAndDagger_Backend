package at.fhv.backend.service;

import at.fhv.backend.model.Game;
import at.fhv.backend.model.Player;
import at.fhv.backend.model.Position;
import at.fhv.backend.model.Role;
import at.fhv.backend.utils.RandomRoleAssigner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private final MapService mapService;

    public PlayerService(MapService mapservice) {
        this.mapService = mapservice;
    }

    public Player createPlayer(String username, Position position, Game game) {
        return new Player(username, position, game);
    }

    public void updatePlayerPosition(Player player, Position newPosition) {
//        System.out.println("updatePlayerPosition called in PlayerService for player: " + player.getId() + " with position: " + newPosition.getX() + ", " + newPosition.getY());
        int x = newPosition.getX();
        int y = newPosition.getY();
        boolean outOfBounds =
                (x < 0) ||
                        (y < 0) ||
                        (y >= mapService.getMap().length) ||
                        (x >= mapService.getMap()[0].length);

        if (mapService != null) {
            if (!outOfBounds && mapService.isCellWalkable(x, y)) { //if true update repo otherwise do nothing
                player.setPosition(newPosition);
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
        //Assign Impostor if player index matches impostor indeces
        if (impostorsIndices.contains(0)) {
            player.setRole(Role.IMPOSTOR);
        }
        return player;
    }

    public List<Player> setRandomRole(List<Player> players) {
        //Create List of Random Indices that will be assigned as Impostors
        List<Integer> impostorsIndices = RandomRoleAssigner.getImpostorsIndices();
        ;
        //Assign Impostor if player index matches impostor indeces
        for (int i = 0; i < players.size(); i++) {
            if (impostorsIndices.contains(i)) {
                players.get(i).setRole(Role.IMPOSTOR);
            }
        }
        return players;
    }

    public Position calculateNewPosition(Position currentPosition, String keyCode) {
        int deltaX = 0, deltaY = 0;
        switch (keyCode) {
            case "KeyA":
                deltaX = -1;
                break;
            case "KeyW":
                deltaY = -1;
                break;
            case "KeyD":
                deltaX = 1;
                break;
            case "KeyS":
                deltaY = 1;
                break;
            default: System.out.println("Invalid key code");
                break;
        }

        int newX = currentPosition.getX() + deltaX;
        int newY = currentPosition.getY() + deltaY;
        return new Position(newX, newY);
    }
}
