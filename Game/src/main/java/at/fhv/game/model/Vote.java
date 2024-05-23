package at.fhv.game.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;

@Getter
@Setter
@ToString
public class Vote {

    @Schema(description = "The game the votes are associated with")
    private String gameCode;

    @Schema(description = "List of all voteEvents, Player that got voted and who voted for them")
    private ArrayList<VoteEvent> voteEvents;

    @Schema(description = "Result of this vote")
    private Integer result;

    public Vote() {
        this.voteEvents = new ArrayList<>();
    }

}
