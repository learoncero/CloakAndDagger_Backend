package at.fhv.game.model.messages;

import at.fhv.game.model.GameMode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateGameMessage {
    private GameMode gameMode;
    private String username;
    private int numberOfPlayers;
    private int numberOfImpostors;
    private String map;
    private String playerColor;

    public CreateGameMessage(GameMode gameMode, String username, int numberOfPlayers, int numberOfImpostors, String map, String playerColor) {
        this.gameMode = gameMode;
        this.username = username;
        this.playerColor = playerColor;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = map;

    }

    public CreateGameMessage() {
    }
}
