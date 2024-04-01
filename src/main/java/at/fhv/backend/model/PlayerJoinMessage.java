package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerJoinMessage {
    private int id;
    private String username;
    private int x;
    private int y;
    private String gameCode;

    public PlayerJoinMessage() {
    }

    public PlayerJoinMessage(int id, String username, int x, int y) {
        this.id = id;
        this.username = username;
        this.x = x;
        this.y = y;
    }

}