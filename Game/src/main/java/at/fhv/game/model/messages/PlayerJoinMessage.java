package at.fhv.game.model.messages;

import at.fhv.game.model.GameMode;
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
    private GameMode gameMode;

    public PlayerJoinMessage() {
    }

    public PlayerJoinMessage(GameMode gameMode, String username, Position position, String playerColor) {
        this.gameMode = gameMode;
        this.username = username;
        this.position = position;
        this.playerColor = playerColor;
    }

}