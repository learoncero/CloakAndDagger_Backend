package at.fhv.minigames.service;

import at.fhv.minigames.model.PasscodeMiniGame;
import at.fhv.minigames.repository.PasscodeMiniGameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PasscodeMiniGameServiceTest {

    @Mock
    private PasscodeMiniGameRepository passcodeMiniGameRepository;
    @InjectMocks
    private PasscodeMiniGameService passcodeMiniGameService;

    @Test
    public void testAddToSum_Success() throws Exception {
        String gameCode = "123";
        int taskId = 456;
        int valueToAdd = 10;
        int expectedCurrentSum = 20;
        PasscodeMiniGame mockMiniGame = new PasscodeMiniGame(1, "Test Game", "Description");
        mockMiniGame.setCurrentSum(10);
        mockMiniGame.setRandomSum(50);

        Mockito.when(passcodeMiniGameRepository.getInstance(gameCode, taskId)).thenReturn(mockMiniGame);

        int actualSum = passcodeMiniGameService.addToSum(valueToAdd, taskId, gameCode);

        assertThat(actualSum).isEqualTo(expectedCurrentSum);
        Mockito.verify(passcodeMiniGameRepository).saveInstance(gameCode, taskId, mockMiniGame);
    }

    @Test
    public void testAddToSum_GameNotFound() throws Exception {
        String gameCode = "123";
        int taskId = 456;
        int valueToAdd = 10;

        Mockito.when(passcodeMiniGameRepository.getInstance(gameCode, taskId)).thenReturn(null);

        int actualSum = passcodeMiniGameService.addToSum(valueToAdd, taskId, gameCode);

        assertThat(actualSum).isEqualTo(-1);
    }
}

