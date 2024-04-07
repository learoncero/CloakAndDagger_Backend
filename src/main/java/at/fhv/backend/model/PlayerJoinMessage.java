package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerJoinMessage {
    private String username;
    private Position position;
    private String gameCode;

    public PlayerJoinMessage() {
    }

    public PlayerJoinMessage(String username, Position position) {
        this.username = username;
        this.position = position;
    }

}