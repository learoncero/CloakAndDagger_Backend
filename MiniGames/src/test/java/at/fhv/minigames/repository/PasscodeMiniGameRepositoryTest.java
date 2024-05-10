package at.fhv.minigames.repository;

import at.fhv.minigames.model.PasscodeMiniGame;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasscodeMiniGameRepositoryTest {

    private PasscodeMiniGameRepository passcodeMiniGameRepository;

    @Before
    public void setUp() {
        passcodeMiniGameRepository = new PasscodeMiniGameRepository();
    }

    @Test
    public void testSaveInstance_Success() {
        String gameCode = "123";
        int taskId = 456;
        PasscodeMiniGame miniGame = new PasscodeMiniGame(1, "Test Game", "Description");
        miniGame.setCurrentSum(10);
        miniGame.setRandomSum(30);

        passcodeMiniGameRepository.saveInstance(gameCode, taskId, miniGame);

        PasscodeMiniGame retrievedGame = passcodeMiniGameRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isEqualTo(miniGame);
    }

    @Test
    public void testGetInstance_Found() {
        String gameCode = "123";
        int taskId = 456;
        PasscodeMiniGame expectedGame = new PasscodeMiniGame(1, "Test Game", "Description");
        expectedGame.setCurrentSum(10);
        expectedGame.setRandomSum(30);

        passcodeMiniGameRepository.saveInstance(gameCode, taskId, expectedGame);

        PasscodeMiniGame retrievedGame = passcodeMiniGameRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isEqualTo(expectedGame);
    }

    @Test
    public void testGetInstance_NotFound() {
        String gameCode = "123";
        int taskId = 456;

        PasscodeMiniGame retrievedGame = passcodeMiniGameRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isNull();
    }

    @Test
    public void testRemoveInstance_Success() {
        String gameCode = "123";
        int taskId = 456;
        PasscodeMiniGame miniGame = new PasscodeMiniGame(1, "Test Game", "Description");
        miniGame.setCurrentSum(10);
        miniGame.setRandomSum(30);

        passcodeMiniGameRepository.saveInstance(gameCode, taskId, miniGame);

        passcodeMiniGameRepository.removeInstance(gameCode, taskId);

        PasscodeMiniGame retrievedGame = passcodeMiniGameRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isNull();
    }
}

