package at.fhv.backend.model.messages;

import at.fhv.backend.model.Position;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerMoveMessage {
    private int id;
    private Position position;
    private String gameCode;

    public PlayerMoveMessage(int id, Position position, String gameCode) {
        this.id = id;
        this.position = position;
        this.gameCode = gameCode;
    }
}