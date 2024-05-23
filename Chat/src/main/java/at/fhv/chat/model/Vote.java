package at.fhv.chat.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Vote {

    @Schema(description = "The game the votes are associated with")
    private String gameCode;

    @Schema(description = "List of all voteEvents, Player that got voted and who voted for them")
    private List<VoteEvent> voteEvents;

    @Schema(description = "Result of this vote")
    private Integer voteResult;

    public Vote(String gameCode) {
        this.gameCode = gameCode;
        this.voteEvents = new ArrayList<>();
    }

    public void addVote(VoteEvent vote) {
        voteEvents.add(vote);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "gameCode='" + gameCode + '\'' +
                ", voteEvents=" + voteEvents +
                ", result=" + voteResult +
                '}';
    }
}
