package at.fhv.minigames.repository;

import static org.junit.jupiter.api.Assertions.*;

import at.fhv.minigames.model.SortingAlgorithmMiniGame;
import at.fhv.minigames.repository.SortingAlgorithmMiniGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SortingAlgorithmMiniGameRepositoryTest {

    private SortingAlgorithmMiniGameRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new SortingAlgorithmMiniGameRepository();
    }

    @Test
    public void testSaveInstanceAndGetInstance() {
        String gameCode = "game1";
        int taskId = 1;
        SortingAlgorithmMiniGame miniGame = new SortingAlgorithmMiniGame();

        repository.saveInstance(gameCode, taskId, miniGame);
        SortingAlgorithmMiniGame retrievedGame = repository.getInstance(gameCode, taskId);

        assertNotNull(retrievedGame);
        assertEquals(miniGame, retrievedGame);
    }

    @Test
    public void testRemoveInstance() {
        String gameCode = "game1";
        int taskId = 1;
        SortingAlgorithmMiniGame miniGame = new SortingAlgorithmMiniGame();

        repository.saveInstance(gameCode, taskId, miniGame);
        repository.removeInstance(gameCode, taskId);
        SortingAlgorithmMiniGame retrievedGame = repository.getInstance(gameCode, taskId);

        assertNull(retrievedGame);
    }

    @Test
    public void testRemoveGameCodeWhenEmpty() {
        String gameCode = "game1";
        int taskId = 1;
        SortingAlgorithmMiniGame miniGame = new SortingAlgorithmMiniGame();

        repository.saveInstance(gameCode, taskId, miniGame);
        repository.removeInstance(gameCode, taskId);
        assertTrue(repository.getTaskMap().isEmpty());
    }
}
