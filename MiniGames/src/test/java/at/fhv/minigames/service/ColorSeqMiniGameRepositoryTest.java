package at.fhv.minigames.service;

import static org.junit.jupiter.api.Assertions.*;

import at.fhv.minigames.model.ColorSeqMiniGame;
import at.fhv.minigames.repository.ColorSeqMiniGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColorSeqMiniGameRepositoryTest {
    private ColorSeqMiniGameRepository repository;
    private String gameCode = "game1";
    private int taskId = 1;
    private ColorSeqMiniGame miniGame;

    @BeforeEach
    void setUp() {
        repository = new ColorSeqMiniGameRepository();
        miniGame = new ColorSeqMiniGame();
    }

    @Test
    void testRepositoryInitialization() {
        assertNotNull(repository.getTaskMap());
    }

    @Test
    void testSaveInstance() {
        repository.saveInstance(gameCode, taskId, miniGame);
        assertTrue(repository.getTaskMap().containsKey(gameCode));
        assertTrue(repository.getTaskMap().get(gameCode).containsKey(taskId));
        assertEquals(miniGame, repository.getTaskMap().get(gameCode).get(taskId));
    }

    @Test
    void testGetInstance_Existing() {
        repository.saveInstance(gameCode, taskId, miniGame);
        ColorSeqMiniGame retrievedMiniGame = repository.getInstance(gameCode, taskId);
        assertNotNull(retrievedMiniGame);
        assertEquals(miniGame, retrievedMiniGame);
    }

    @Test
    void testGetInstance_NonExisting() {
        ColorSeqMiniGame retrievedMiniGame = repository.getInstance(gameCode, 999);
        assertNull(retrievedMiniGame);
    }

    @Test
    void testRemoveInstance_Existing() {
        repository.saveInstance(gameCode, taskId, miniGame);
        repository.removeInstance(gameCode, taskId);
        assertFalse(repository.getTaskMap().get(gameCode).containsKey(taskId));
    }

    @Test
    void testRemoveInstance_NonExisting() {
        repository.removeInstance(gameCode, 999);
        assertNull(repository.getTaskMap().get(gameCode));
    }
}
