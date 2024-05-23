package at.fhv.minigames.service;

import at.fhv.minigames.model.MovingSquareMiniGame;
import at.fhv.minigames.repository.MovingSquareMiniGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MovingSquareMiniGameServiceTest {
    @Mock
    private MovingSquareMiniGameRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MovingSquareMiniGameService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInstance() {
        String gameCode = "game1";
        int taskId = 1;
        MovingSquareMiniGame miniGame = new MovingSquareMiniGame();

        when(repository.getInstance(gameCode, taskId)).thenReturn(miniGame);

        MovingSquareMiniGame result = service.getInstance(gameCode, taskId);
        assertNotNull(result);
        assertEquals(miniGame, result);

        verify(repository, times(1)).getInstance(gameCode, taskId);
    }

    @Test
    void testMarkAsCompletedFail() {
        String gameCode = "game1";
        int taskId = 1;

        when(repository.getInstance(gameCode, taskId)).thenReturn(null);

        boolean result = service.markAsCompleted(gameCode, taskId);
        assertFalse(result);

        verify(repository, times(1)).getInstance(gameCode, taskId);
        verify(restTemplate, never()).postForEntity(anyString(), any(), any());
        verify(repository, never()).removeInstance(gameCode, taskId);
    }

    @Test
    void testSaveInstance() {
        String gameCode = "game1";
        int taskId = 1;
        MovingSquareMiniGame miniGame = new MovingSquareMiniGame();

        service.saveInstance(gameCode, taskId, miniGame);

        verify(repository, times(1)).saveInstance(gameCode, taskId, miniGame);
    }

    @Test
    void testDeleteInstance() {
        String gameCode = "game1";
        int taskId = 1;

        service.deleteInstance(gameCode, taskId);

        verify(repository, times(1)).removeInstance(gameCode, taskId);
    }
}
