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

    @Schema(description = "List of all votes")
    private List<Integer> votes;

    public Vote(String gameCode) {
        this.gameCode = gameCode;
        this.votes = new ArrayList<>();
    }

    public void addVote(int vote) {
        votes.add(vote);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "votes=" + votes +
                '}';
    }
}
