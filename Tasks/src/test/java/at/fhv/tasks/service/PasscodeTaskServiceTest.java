package at.fhv.tasks.service;

import at.fhv.tasks.model.PasscodeMiniGame;
import at.fhv.tasks.repository.PasscodeTaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class PasscodeTaskServiceTest {

    @Mock
    private PasscodeTaskRepository passcodeTaskRepository;
    @InjectMocks
    private PasscodeTaskService passcodeTaskService;

    @Test
    public void testAddToSum_Success() throws Exception {
        String gameCode = "123";
        int taskId = 456;
        int valueToAdd = 10;
        int expectedCurrentSum = 20;
        PasscodeMiniGame mockMiniGame = new PasscodeMiniGame(1, "Test Game", "Description");
        mockMiniGame.setCurrentSum(10);
        mockMiniGame.setRandomSum(50);

        Mockito.when(passcodeTaskRepository.getInstance(gameCode, taskId)).thenReturn(mockMiniGame);

        int actualSum = passcodeTaskService.addToSum(valueToAdd, taskId, gameCode);

        assertThat(actualSum).isEqualTo(expectedCurrentSum);
        Mockito.verify(passcodeTaskRepository).saveInstance(gameCode, taskId, mockMiniGame);
    }

    /*
    @Test
    public void testAddToSum_ExceedTargetSum() throws Exception {
        String gameCode = "123";
        int taskId = 456;
        int valueToAdd = 50;
        int mockRandomSum = 30;
        PasscodeMiniGame mockMiniGame = new PasscodeMiniGame(1, "Test Game", "Description");
        mockMiniGame.setCurrentSum(10);
        mockMiniGame.setRandomSum(mockRandomSum);

        Mockito.when(passcodeTaskRepository.getInstance(gameCode, taskId)).thenReturn(mockMiniGame);
        Mockito.verifyNoInteractions(passcodeTaskRepository.saveInstance(any(), any(), any())); // No save expected

        int actualSum = passcodeTaskService.addToSum(valueToAdd, taskId, gameCode);

        assertThat(actualSum).isEqualTo(0);
    }
     */

    @Test
    public void testAddToSum_GameNotFound() throws Exception {
        String gameCode = "123";
        int taskId = 456;
        int valueToAdd = 10;

        Mockito.when(passcodeTaskRepository.getInstance(gameCode, taskId)).thenReturn(null);

        int actualSum = passcodeTaskService.addToSum(valueToAdd, taskId, gameCode);

        assertThat(actualSum).isEqualTo(-1);
    }
}

