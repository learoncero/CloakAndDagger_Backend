package at.fhv.backend.service;

import at.fhv.backend.model.*;
import at.fhv.backend.utils.RandomRoleAssigner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    public PlayerService() {
    }

    public Player createPlayer(String username, Position randomPosition, Game game) {
        Player player = new Player(username, randomPosition, game);

        return player;
    }

    public void updatePlayerPosition(Player player, Position newPosition, Map map) {
        int x = newPosition.getX();
        int y = newPosition.getY();
        boolean outOfBounds =
                (x < 0) ||
                        (y < 0) ||
                        (y >= map.getMap().length) ||
                        (x >= map.getMap()[0].length);

        if (!outOfBounds && isCellWalkable(map, x, y)) { //if true update repo otherwise do nothing
            player.setPosition(newPosition);
        }
    }

    private boolean isCellWalkable(Map map, int x, int y) {
        return map.getCellValue(x, y);
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
}
