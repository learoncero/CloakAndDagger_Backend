/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          14/05/2024
 */

package at.fhv.chat.service;

import at.fhv.chat.model.Vote;
import at.fhv.chat.model.VoteEvent;
import at.fhv.chat.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VoteServiceTest {

    private VoteService voteService;
    private VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        voteRepository = Mockito.mock(VoteRepository.class);
        voteService = new VoteService(voteRepository);
    }

    @AfterEach
    void tearDown() {
        // Assuming VoteRepository has a method to clear all votes
        voteRepository.clearAllVotes();
    }

    @Test
    void testStartVote() {
        String gameCode = "game1";
        Vote vote = new Vote(gameCode);

        voteService.startVote(gameCode);

        verify(voteRepository, times(1)).addVotes(any(Vote.class));

    }

    @Test
    void testAddVote() {
        String gameCode = "game1";
        Vote vote = new Vote(gameCode);
        VoteEvent voteEvent = new VoteEvent(1, 2);

        when(voteRepository.getVote(anyString())).thenReturn(vote);

        Vote result = voteService.addVote(gameCode, voteEvent);

        assertEquals(vote, result);
        verify(voteRepository, times(1)).getVote(anyString());
    }

    @Test
    void testGetVoteResult() {
        String gameCode = "game1";
        Vote vote = new Vote(gameCode);
        VoteEvent voteEvent = new VoteEvent(1, 2);

        vote.addVote(voteEvent);

        when(voteRepository.getVote(anyString())).thenReturn(vote);

        Integer result = voteService.getVoteResult(gameCode);

        assertEquals(1, result);
        verify(voteRepository, times(1)).getVote(anyString());
    }

    @Test
    void testGetVoteResultTie() {
        String gameCode = "game1";
        Vote vote = new Vote(gameCode);
        VoteEvent voteEvent1 = new VoteEvent(1, 2);
        VoteEvent voteEvent2 = new VoteEvent(2, 3);
        VoteEvent voteEvent3 = new VoteEvent(1, 4);
        VoteEvent voteEvent4 = new VoteEvent(3, 1);
        VoteEvent voteEvent5 = new VoteEvent(2, 5);
        vote.addVote(voteEvent1);
        vote.addVote(voteEvent2);
        vote.addVote(voteEvent3);
        vote.addVote(voteEvent4);
        vote.addVote(voteEvent5);

        when(voteRepository.getVote(anyString())).thenReturn(vote);

        Integer result = voteService.getVoteResult(gameCode);

        assertEquals(-1, result);
        verify(voteRepository, times(1)).getVote(anyString());
    }

    @Test
    void testGetVoteResultWin() {
        String gameCode = "game1";
        Vote vote = new Vote(gameCode);
        VoteEvent voteEvent1 = new VoteEvent(1, 2);
        VoteEvent voteEvent2 = new VoteEvent(2, 3);
        VoteEvent voteEvent3 = new VoteEvent(1, 4);
        vote.addVote(voteEvent1);
        vote.addVote(voteEvent2);
        vote.addVote(voteEvent3);

        when(voteRepository.getVote(anyString())).thenReturn(vote);

        Integer result = voteService.getVoteResult(gameCode);

        assertEquals(1, result);
        verify(voteRepository, times(1)).getVote(anyString());
    }

    @Test
    void testGetVoteResultNoVotes() {
        String gameCode = "game1";
        Vote vote = new Vote(gameCode);

        when(voteRepository.getVote(anyString())).thenReturn(vote);

        Integer result = voteService.getVoteResult(gameCode);

        assertEquals(0, result);
        verify(voteRepository, times(1)).getVote(anyString());
    }

    @Test
    void testEndVote() {
        String gameCode = "game1";
        Vote vote = new Vote(gameCode);

        when(voteRepository.getVote(anyString())).thenReturn(vote);

        voteService.endVote(gameCode);

        verify(voteRepository, times(1)).getVote(anyString());
        verify(voteRepository, times(1)).removeVote(any(Vote.class));
    }
}