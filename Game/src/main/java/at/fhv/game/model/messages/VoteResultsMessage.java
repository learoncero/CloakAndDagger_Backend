package at.fhv.game.model.messages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VoteResultsMessage {
    private String gameCode;
    private int playerToEliminateId;

    public VoteResultsMessage(String gameCode, int playerToEliminateId) {
        this.gameCode = gameCode;
        this.playerToEliminateId = playerToEliminateId;
    }
}
