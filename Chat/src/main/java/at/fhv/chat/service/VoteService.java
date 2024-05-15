package at.fhv.chat.service;

import at.fhv.chat.model.Vote;

import at.fhv.chat.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VoteService {
    VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote startVote(String gameCode) {
        Vote vote = new Vote(gameCode);
        voteRepository.addVotes(vote);
        return vote;
    }

    public Vote addVote(String gameCode, int vote) {
        Vote currentVotes = voteRepository.getVote(gameCode);
        currentVotes.addVote(vote);
        return currentVotes;
    }

    public Integer getVoteResult(String gameCode) {
        Vote currentVotes = voteRepository.getVote(gameCode);
        if (currentVotes != null) {
            List<Integer> votes = currentVotes.getVotes();
            if (votes.isEmpty()) {
                return 0;
            }
            Collections.sort(votes); //sorts the list in ascending order

            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (int num : votes) {
                frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1); //key = vote(PlayerId), value = voteCount
            }
            int maxCount = 0;
            int mostFrequent = -1;
            boolean isTie = false;
            for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
                int count = entry.getValue();
                if (count > maxCount) {
                    maxCount = count;
                    mostFrequent = entry.getKey();
                    isTie = false;
                } else if (count == maxCount) {
                    isTie = true;
                }
            }
            // If there is a tie, return -1
            if (isTie) {
                return -1;
            }
            return mostFrequent;
        }else {
            return -2;
        }
    }

    public Vote endVote(String gameCode) {
        Vote vote = voteRepository.getVote(gameCode);
        voteRepository.removeVote(vote);
        return vote;
    }
}
