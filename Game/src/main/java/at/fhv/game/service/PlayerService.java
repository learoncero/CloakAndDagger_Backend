package at.fhv.game.service;

import at.fhv.game.model.*;
import at.fhv.game.model.messages.PlayerJoinMessage;
import at.fhv.game.model.messages.PlayerMoveMessage;
import at.fhv.game.utils.RandomRoleAssigner;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    private List<Integer> impostorIndices;

    public PlayerService() {
        this.impostorIndices = new ArrayList<>();
    }

    public Player createPlayer(String username, Position randomPosition, Game game, String playerColor) {

        Player player = new Player(username, randomPosition, game, playerColor );

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
        return map.getCellValue(x, y) == '.';
    }

    public Player setInitialRandomRole(int numPlayers, int numImpostors, Player player) {
        //Create List of Random Indices that will be assigned as Impostors
        impostorIndices = RandomRoleAssigner.assignRandomRoles(numPlayers, numImpostors);
        //Assign Impostor if player index matches impostor indeces
        if (impostorIndices.contains(0)) {
            player.setRole(Role.IMPOSTOR);
        }
        return player;
    }

    public List<Player> setRandomRole(List<Player> players) {

        for (int i = 0; i < players.size(); i++) {
            if (impostorIndices.contains(i)) {
                players.get(i).setRole(Role.IMPOSTOR);
            }
        }
        return players;
    }

    public Position calculateNewPosition(Position currentPosition, String keyCode, List<Sabotage> sabotages, Player player) {
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
            default:
                System.out.println("Invalid key code");
                break;
        }

        int newX = currentPosition.getX() + deltaX;
        int newY = currentPosition.getY() + deltaY;
        Position newPosition = new Position(newX, newY);

        if (shouldMirrorMovement(sabotages, player)) {
            System.out.println("Should it really mirror?");
            newPosition = mirrorPosition(currentPosition, newPosition);
        }

        return newPosition;
    }

    private boolean shouldMirrorMovement(List<Sabotage> sabotages, Player player) {

        for (Sabotage sabotage : sabotages) {

            if (sabotage.getId() == 1 && sabotage.getPosition().getY() != -1 && player.getRole() == Role.CREWMATE) {
                if(player.isMirrored() == false) {
                    updatePlayerMirrored(player, true);
                } else {
                    updatePlayerMirrored(player, false);
                }
                return true;
            }
        }
        return false;
    }

    private Position mirrorPosition(Position currentPosition, Position newPosition) {
        int deltaX = newPosition.getX() - currentPosition.getX();
        int deltaY = newPosition.getY() - currentPosition.getY();

        // Mirror the movement
        int mirroredX = currentPosition.getX() - deltaX;
        int mirroredY = currentPosition.getY() - deltaY;

        return new Position(mirroredX, mirroredY);
    }

    public void updatePlayerMirrored(Player player , boolean isMirrored) {
        if (player != null) {
            player.setMirrored(isMirrored);


        }
    }
    public void updatePlayerisMoving(Player player , boolean isMoving) {
        if (player != null) {
            player.setMoving(isMoving);


        }
    }
}
