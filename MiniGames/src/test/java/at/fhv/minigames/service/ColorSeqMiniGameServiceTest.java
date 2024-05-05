package at.fhv.minigames.service;

import at.fhv.minigames.model.ColorSeqMiniGame;
import at.fhv.minigames.repository.ColorSeqMiniGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ColorSequenceMiniGameServiceTest {

    @Mock
    private ColorSeqMiniGameRepository colorSeqMiniGameRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ColorSequenceMiniGameService colorSequenceMiniGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetInstance() {
        String gameCode = "game1";
        int taskId = 1;
        ColorSeqMiniGame expectedMiniGame = new ColorSeqMiniGame();
        when(colorSeqMiniGameRepository.getInstance(gameCode, taskId)).thenReturn(expectedMiniGame);

        ColorSeqMiniGame result = colorSequenceMiniGameService.getInstance(gameCode, taskId);

        assertEquals(expectedMiniGame, result);
        verify(colorSeqMiniGameRepository).getInstance(gameCode, taskId);
    }

    @Test
    void testCreateShuffledSequence() {
        List<String> colors = Arrays.asList("red", "blue", "green", "yellow");
        List<String> original = List.copyOf(colors);

        List<String> shuffled = colorSequenceMiniGameService.createShuffledSequence(colors);

        assertNotEquals(original, shuffled);
    }

    @Test
    void testVerifySequence_Correct() {
        String gameCode = "game1";
        int taskId = 1;
        List<String> correctSequence = Arrays.asList("red", "blue", "green", "yellow");
        ColorSeqMiniGame miniGame = new ColorSeqMiniGame();

        when(colorSeqMiniGameRepository.getInstance(gameCode, taskId)).thenReturn(miniGame);

        boolean result = colorSequenceMiniGameService.verifySequence(correctSequence, correctSequence, taskId, gameCode);

        assertTrue(result);
        verify(restTemplate).postForEntity(anyString(), eq(taskId), eq(Void.class));
        verify(colorSeqMiniGameRepository).removeInstance(gameCode, taskId);
    }

    @Test
    void testVerifySequence_Incorrect() {
        String gameCode = "game2";
        int taskId = 2;
        List<String> submitted = Arrays.asList("red", "green", "blue", "yellow");
        List<String> correct = Arrays.asList("red", "blue", "green", "yellow");
        ColorSeqMiniGame miniGame = new ColorSeqMiniGame();

        when(colorSeqMiniGameRepository.getInstance(gameCode, taskId)).thenReturn(miniGame);

        boolean result = colorSequenceMiniGameService.verifySequence(submitted, correct, taskId, gameCode);

        assertFalse(result);
        verify(colorSeqMiniGameRepository).saveInstance(gameCode, taskId, miniGame);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void testSaveNewInstance() {
        String gameCode = "game3";
        int taskId = 3;
        ColorSeqMiniGame miniGame = new ColorSeqMiniGame();

        colorSequenceMiniGameService.saveNewInstance(gameCode, taskId, miniGame);

        verify(colorSeqMiniGameRepository).saveInstance(gameCode, taskId, miniGame);
    }
}
