package at.fhv.game.model.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerMoveMessage {
    private int id;
    private String keyCode;
    private String gameCode;
    private boolean isMirrored;
    private boolean isMoving;


    public PlayerMoveMessage(int id, String keyCode, String gameCode, boolean Mirrored, boolean isMoving) {
        this.id = id;
        this.keyCode = keyCode;
        this.gameCode = gameCode;
        this.isMirrored = Mirrored;
        this.isMoving = isMoving;
    }
}