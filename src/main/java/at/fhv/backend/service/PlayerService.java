package at.fhv.backend.service;

import at.fhv.backend.model.*;
import at.fhv.backend.repository.MapRepository;
import at.fhv.backend.utils.MapLoader;
import at.fhv.backend.utils.RandomRoleAssigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private final MapService mapService;
    private final GameService gameService;

    public PlayerService(MapService mapservice, GameService gameService) {
        this.mapService = mapservice;
        this.gameService = gameService;
    }

    public Player createPlayer(String username, Position position, Game game) {
        return new Player(username, position, game);
    }

    public void updatePlayerPosition(Player player, Position newPosition, String gameCode) {
        String mapName = gameService.getGameByCode(gameCode).getMap();
        int x = newPosition.getX();
        int y = newPosition.getY();
        boolean outOfBounds =
                (x < 0) ||
                        (y < 0) ||
                        (y >= mapService.getMap(mapName).getMap().length) ||
                        (x >= mapService.getMap(mapName).getMap()[0].length);

        if (mapService != null) {
            if (!outOfBounds && mapService.isCellWalkable(mapName, x, y)) { //if true update repo otherwise do nothing
                player.setPosition(newPosition);
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
}
