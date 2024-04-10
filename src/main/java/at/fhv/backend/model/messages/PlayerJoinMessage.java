package at.fhv.backend.model.messages;

import at.fhv.backend.model.Position;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerJoinMessage {
    private String username;
    private Position position;
    private String gameCode;
    private String uuid;

    public PlayerJoinMessage() {
    }

    public PlayerJoinMessage(String username, Position position) {
        this.username = username;
        this.position = position;
    }

}