package at.fhv.game.model.messages;

import at.fhv.game.model.Player;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateGameMessage {
    private Player player;
    private int numberOfPlayers;
    private int numberOfImpostors;
    private String map;
    private String playerColor;

    public CreateGameMessage(Player player, int numberOfPlayers, int numberOfImpostors, String map, String playerColor) {

        this.player = player;
        this.playerColor = playerColor;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = map;

    }

    public CreateGameMessage() {
    }
}
