package at.fhv.backend.model;


import at.fhv.backend.model.Position;


public class PlayerMoveMessage {
    private int id;
    private Position newPosition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Position newPosition) {
        this.newPosition = newPosition;
    }
}