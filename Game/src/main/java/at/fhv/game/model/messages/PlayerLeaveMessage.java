package at.fhv.game.model.messages;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerLeaveMessage {
    private String username;

    public PlayerLeaveMessage() {
    }

    public PlayerLeaveMessage(String username) {
        this.username = username;
    }
}
