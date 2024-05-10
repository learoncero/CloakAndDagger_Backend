package at.fhv.minigames.repository;

import at.fhv.minigames.model.DecipherSymbolsMiniGame;
import at.fhv.minigames.model.PasscodeMiniGame;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DecipherSymbolsMiniGameRepositoryTest {

    private DecipherSymbolsMiniGameRepository decipherSymbolsMiniGameRepository;

    @Before
    public void setUp() {
        decipherSymbolsMiniGameRepository = new DecipherSymbolsMiniGameRepository();
    }

    @Test
    public void testSaveInstance_Success() {
        String gameCode = "123";
        int taskId = 456;
        DecipherSymbolsMiniGame miniGame = new DecipherSymbolsMiniGame(3, "Test Game", "Description");

        decipherSymbolsMiniGameRepository.saveInstance(gameCode, taskId, miniGame);

        DecipherSymbolsMiniGame retrievedGame = decipherSymbolsMiniGameRepository.getInstance(gameCode, taskId);

        assertEquals(miniGame, retrievedGame);
    }

    @Test
    public void testGetInstance_Found() {
        String gameCode = "123";
        int taskId = 456;
        DecipherSymbolsMiniGame expectedGame = new DecipherSymbolsMiniGame(3, "Test Game", "Description");

        decipherSymbolsMiniGameRepository.saveInstance(gameCode, taskId, expectedGame);

        DecipherSymbolsMiniGame retrievedGame = decipherSymbolsMiniGameRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isEqualTo(expectedGame);
    }

    @Test
    public void testGetInstance_NotFound() {
        String gameCode = "123";
        int taskId = 456;

        DecipherSymbolsMiniGame retrievedGame = decipherSymbolsMiniGameRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isNull();
    }

    @Test
    public void testRemoveInstance_Success() {
        String gameCode = "123";
        int taskId = 456;
        DecipherSymbolsMiniGame miniGame = new DecipherSymbolsMiniGame(3, "Test Game", "Description");

        decipherSymbolsMiniGameRepository.saveInstance(gameCode, taskId, miniGame);

        decipherSymbolsMiniGameRepository.removeInstance(gameCode, taskId);

        DecipherSymbolsMiniGame retrievedGame = decipherSymbolsMiniGameRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isNull();
    }
}
