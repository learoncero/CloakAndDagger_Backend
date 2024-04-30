package at.fhv.tasks.service;

import at.fhv.tasks.model.PasscodeMiniGame;
import at.fhv.tasks.repository.PasscodeTaskRepository;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasscodeTaskRepositoryTest {

    private PasscodeTaskRepository passcodeTaskRepository;

    @Before
    public void setUp() {
        passcodeTaskRepository = new PasscodeTaskRepository();
    }

    @Test
    public void testSaveInstance_Success() {
        String gameCode = "123";
        int taskId = 456;
        PasscodeMiniGame miniGame = new PasscodeMiniGame(1, "Test Game", "Description");
        miniGame.setCurrentSum(10);
        miniGame.setRandomSum(30);

        passcodeTaskRepository.saveInstance(gameCode, taskId, miniGame);

        PasscodeMiniGame retrievedGame = passcodeTaskRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isEqualTo(miniGame);
    }

    @Test
    public void testGetInstance_Found() {
        String gameCode = "123";
        int taskId = 456;
        PasscodeMiniGame expectedGame = new PasscodeMiniGame(1, "Test Game", "Description");
        expectedGame.setCurrentSum(10);
        expectedGame.setRandomSum(30);

        passcodeTaskRepository.saveInstance(gameCode, taskId, expectedGame);

        PasscodeMiniGame retrievedGame = passcodeTaskRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isEqualTo(expectedGame);
    }

    @Test
    public void testGetInstance_NotFound() {
        String gameCode = "123";
        int taskId = 456;

        PasscodeMiniGame retrievedGame = passcodeTaskRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isNull();
    }

    @Test
    public void testRemoveInstance_Success() {
        String gameCode = "123";
        int taskId = 456;
        PasscodeMiniGame miniGame = new PasscodeMiniGame(1, "Test Game", "Description");
        miniGame.setCurrentSum(10);
        miniGame.setRandomSum(30);

        passcodeTaskRepository.saveInstance(gameCode, taskId, miniGame);

        passcodeTaskRepository.removeInstance(gameCode, taskId);

        PasscodeMiniGame retrievedGame = passcodeTaskRepository.getInstance(gameCode, taskId);

        assertThat(retrievedGame).isNull();
    }
}

