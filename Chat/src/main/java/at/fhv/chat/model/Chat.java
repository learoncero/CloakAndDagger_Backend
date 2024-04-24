package at.fhv.chat.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Chat {
    private String id;
    private List<String> activePlayersUsernames;

    public Chat(String gameCode, List<String> activePlayersUsernames) {
        this.id = "Chat for game with game code: " + gameCode;
        this.activePlayersUsernames = activePlayersUsernames;
    }
}
