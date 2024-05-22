package at.fhv.game.model.messages;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateGameMessage {
    private String username;
    private int numberOfPlayers;
    private int numberOfImpostors;
    private String map;
    private String playerColor;

    public CreateGameMessage(String username, int numberOfPlayers, int numberOfImpostors, String map, String playerColor) {

        this.username = username;
        this.playerColor = playerColor;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = map;

    }

    public CreateGameMessage() {
    }
}
