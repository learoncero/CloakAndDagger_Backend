package at.fhv.game.model.messages;

import at.fhv.game.model.Position;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerJoinMessage {
    private String username;
    private Position position;
    private String gameCode;

    private String playerColor;

    public PlayerJoinMessage() {
    }

    public PlayerJoinMessage(String username, Position position, String playerColor) {
        this.username = username;
        this.position = position;
        this.playerColor = playerColor;
    }

}