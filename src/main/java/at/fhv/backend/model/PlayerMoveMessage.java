package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerMoveMessage {
    private int id;
    private String keyCode;
    private String gameCode;

    public PlayerMoveMessage(int id, String keyCode, String gameCode) {
        this.id = id;
        this.keyCode = keyCode;
        this.gameCode = gameCode;
    }
}