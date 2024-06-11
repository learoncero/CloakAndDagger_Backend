package at.fhv.game.service;

import at.fhv.game.model.*;
import at.fhv.game.utils.RandomRoleAssigner;
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

        return new Player(username, randomPosition, game, playerColor);
    }

    public void updatePlayerPosition(Player player, Position newPosition, Map map, List<Sabotage> sabotages, List<Player> players) {
        int x = newPosition.getX();
        int y = newPosition.getY();
        boolean outOfBounds =
                (x < 0) ||
                        (y < 0) ||
                        (y >= map.getMap().length) ||
                        (x >= map.getMap()[0].length);

        if (!outOfBounds && isCellWalkable(map, x, y, player.getRole(), sabotages, players)) {
            player.setPlayerPosition(newPosition);
        }
    }

    public Player setInitialRandomRole(int numPlayers, int numImpostors, Player player) {
        //Create List of Random Indices that will be assigned as Impostors
        impostorIndices = RandomRoleAssigner.assignRandomRoles(numPlayers, numImpostors);
        //Assign Impostor if player index matches impostor indexes
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
                break;
        }

        int newX = currentPosition.getX() + deltaX;
        int newY = currentPosition.getY() + deltaY;
        Position newPosition = new Position(newX, newY);

        if (shouldMirrorMovement(sabotages, player)) {
            newPosition = mirrorPosition(currentPosition, newPosition);
        }

        return newPosition;
    }

    private boolean shouldMirrorMovement(List<Sabotage> sabotages, Player player) {

        for (Sabotage sabotage : sabotages) {

            if (sabotage.getId() == 2 && sabotage.getPosition().getY() != -1 && (player.getRole() == Role.CREWMATE || player.getRole() == Role.CREWMATE_GHOST)) {
                updatePlayerMirrored(player, !player.isMirrored());
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

    public void updatePlayerMirrored(Player player, boolean isMirrored) {
        if (player != null) {
            player.setMirrored(isMirrored);
        }
    }

    public void updatePlayerisMoving(Player player, boolean isMoving) {
        if (player != null) {
            player.setMoving(isMoving);
        }
    }

    private boolean isWallPosition(List<Sabotage> sabotages, int x, int y) {
        for (Sabotage sabotage : sabotages) {
            if (sabotage.getId() == 4 && sabotage.getWallPositions() != null) {
                for (Position[] wallPositions : sabotage.getWallPositions()) {
                    for (Position wallPosition : wallPositions) {
                        if (wallPosition.getX() == x && wallPosition.getY() == y) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isPlayerPositionFree(List<Player> players, Position newPosition, Role playerRole) {
        if (playerRole == Role.IMPOSTOR_GHOST || playerRole == Role.CREWMATE_GHOST) {
            return true;
        }

        for (Player player : players) {
            if ((player.getPlayerPosition().equals(newPosition) && player.getRole() != Role.IMPOSTOR_GHOST && player.getRole() != Role.CREWMATE_GHOST) || player.getDeadBodyPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCellWalkable(Map map, int x, int y, Role playerRole, List<Sabotage> sabotages, List<Player> players) {
        if (isWallPosition(sabotages, x, y)) {
            return false;
        }

        Position newPosition = new Position(x, y);

        if (playerRole == Role.IMPOSTOR || playerRole == Role.CREWMATE) {
            // Check for player collisions
            if (isPlayerPositionFree(players, newPosition, playerRole)) {
                return map.getCellValue(x, y) == '.' || Character.isDigit(map.getCellValue(x, y));
            } else {
                return false;
            }
        } else if (playerRole == Role.IMPOSTOR_GHOST || playerRole == Role.CREWMATE_GHOST) {
            return map.getCellValue(x, y) == '.' || map.getCellValue(x, y) == '#' || Character.isDigit(map.getCellValue(x, y));
        }
        return false;
    }

    public Position sendPlayerToVent(Player player, List<Pair<Position>> ventPositions) {

        if (player != null) {
            Position currentPosition = player.getPlayerPosition();
            for (Pair<Position> vP : ventPositions) {
                if ((Math.abs(vP.getFirst().getX() - currentPosition.getX()) <= 1) &&
                        (Math.abs(vP.getFirst().getY() - currentPosition.getY()) <= 1)) {
                    return vP.getSecond();
                } else if ((Math.abs(vP.getSecond().getX() - currentPosition.getX()) <= 1) &&
                        (Math.abs(vP.getSecond().getY() - currentPosition.getY()) <= 1)) {
                    return vP.getFirst();
                }
            }
        }

        assert player != null;
        return player.getPlayerPosition();
    }
}
