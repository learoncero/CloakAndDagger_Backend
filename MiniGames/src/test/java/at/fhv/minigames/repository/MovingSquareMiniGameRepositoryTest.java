package at.fhv.minigames.repository;

import at.fhv.minigames.model.MovingSquareMiniGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovingSquareMiniGameRepositoryTest {
    private MovingSquareMiniGameRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MovingSquareMiniGameRepository();
    }

    @Test
    void testSaveInstanceAndGetInstance() {
        String gameCode = "game1";
        int taskId = 1;
        MovingSquareMiniGame miniGame = new MovingSquareMiniGame();
        repository.saveInstance(gameCode, taskId, miniGame);

        MovingSquareMiniGame retrievedGame = repository.getInstance(gameCode, taskId);
        assertNotNull(retrievedGame);
        assertEquals(miniGame, retrievedGame);
    }

    @Test
    void testGetInstanceNotFound() {
        String gameCode = "game1";
        int taskId = 1;

        MovingSquareMiniGame retrievedGame = repository.getInstance(gameCode, taskId);
        assertNull(retrievedGame);
    }

    @Test
    void testRemoveInstance() {
        String gameCode = "game1";
        int taskId = 1;
        MovingSquareMiniGame miniGame = new MovingSquareMiniGame();
        repository.saveInstance(gameCode, taskId, miniGame);

        repository.removeInstance(gameCode, taskId);
        MovingSquareMiniGame retrievedGame = repository.getInstance(gameCode, taskId);
        assertNull(retrievedGame);
    }
}
