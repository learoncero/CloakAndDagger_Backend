package at.fhv.chat.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Chat {
    @Schema(description = "The unique identifier for the chat")
    private String id;

    @Schema(description = "List of usernames of active players in the chat")
    private List<String> activePlayersUsernames;

    public Chat(String gameCode, List<String> activePlayersUsernames) {
        this.id = "Chat for game with game code: " + gameCode;
        this.activePlayersUsernames = activePlayersUsernames;
    }
}
