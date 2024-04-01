package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateGameMessage {
    private String username;
    private String numberOfPlayers;
    private String numberOfImpostors;
    private String map;

    public CreateGameMessage(String username, String numberOfPlayers, String numberOfImpostors, String map) {
        this.username = username;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = map;
    }

    public CreateGameMessage() {}
}
