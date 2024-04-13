package at.fhv.backend.model.messages;

import at.fhv.backend.model.Player;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateGameMessage {
    private Player player;
    private int numberOfPlayers;
    private int numberOfImpostors;
    private String map;

    public CreateGameMessage(Player player, int numberOfPlayers, int numberOfImpostors, String map) {
        this.player = player;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = map;
    }

    public CreateGameMessage() {
    }
}
