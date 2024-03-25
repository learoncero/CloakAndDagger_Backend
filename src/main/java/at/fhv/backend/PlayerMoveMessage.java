package at.fhv.backend;

import at.fhv.backend.model.Player;
import at.fhv.backend.model.Position;

public class PlayerMoveMessage {
    private int playerID;
    private Position newPosition;

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public Position getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Position newPosition) {
        this.newPosition = newPosition;
    }
}