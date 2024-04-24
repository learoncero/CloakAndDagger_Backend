package at.fhv.chat.model.messages;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatStartMessage {
    private String gameCode;
    private List<String> activePlayersUsernames;

    public ChatStartMessage() {
    }

    public ChatStartMessage(String gameCode, List<String> activePlayersUsernames) {
        this.gameCode = gameCode;
        this.activePlayersUsernames = activePlayersUsernames;
    }
}
