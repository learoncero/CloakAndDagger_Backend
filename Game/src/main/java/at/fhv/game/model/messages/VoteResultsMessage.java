package at.fhv.game.model.messages;

import at.fhv.game.model.VoteEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VoteResultsMessage {
    private String gameCode;
    private List<VoteEvent> voteEvents;
    private Integer voteResult;

    public VoteResultsMessage(String gameCode, List<VoteEvent> voteEvents, int voteResult) {
        this.gameCode = gameCode;
        this.voteEvents = voteEvents;
        this.voteResult = voteResult;
    }
}