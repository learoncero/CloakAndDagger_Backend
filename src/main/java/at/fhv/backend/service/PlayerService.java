package at.fhv.backend.service;

import at.fhv.backend.model.Game;
import at.fhv.backend.model.Player;
import at.fhv.backend.model.Position;
import org.springframework.stereotype.Service;

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
        System.out.println("updatePlayerPosition called in PlayerService for player: " + player.getId() + " with position: " + newPosition.getX() + ", " + newPosition.getY());
        int x = newPosition.getX();
        int y = newPosition.getY();
        boolean outOfBounds =
                (x < 0) ||
                (y < 0) ||
                (y >= mapService.getMap().length) ||
                (x >= mapService.getMap()[0].length);

        if (mapService != null){
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
