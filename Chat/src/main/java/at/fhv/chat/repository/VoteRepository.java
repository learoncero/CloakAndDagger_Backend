package at.fhv.chat.repository;

import at.fhv.chat.model.Vote;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VoteRepository {
    List<Vote> allVotes;

    public VoteRepository() {
        this.allVotes = new ArrayList<>();
    }

    public void addVotes(Vote vote) {
        allVotes.add(vote);
    }

    public Vote getVote(String gameCode) {
        return allVotes.stream()
                .filter(vote -> vote.getGameCode().equals(gameCode))
                .findFirst()
                .orElse(null);
    }

    public void removeVote(Vote vote) {
        allVotes.remove(vote);
    }
}
