package at.fhv.game.model.messages;

import at.fhv.game.model.GameMode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerJoinMessage {
    private String username;
    private String gameCode;
    private String playerColor;
    private GameMode gameMode;

    public PlayerJoinMessage() {
    }

    public PlayerJoinMessage(String username, String gameCode, String playerColor, GameMode gameMode) {
        this.username = username;
        this.gameCode = gameCode;
        this.playerColor = playerColor;
        this.gameMode = gameMode;
    }

}