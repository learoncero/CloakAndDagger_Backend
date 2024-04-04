package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerJoinMessage {
    private int id;
    private String username;
    private Position position;
    private String gameCode;

    public PlayerJoinMessage() {
    }

    public PlayerJoinMessage(int id, String username, Position position) {
        this.id = id;
        this.username = username;
        this.position = position;
    }

}